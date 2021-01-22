import React, { useState } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { sellerLoading, sellerDone } from "../../info/SellerInfo";
import {
  Avatar,
  Button,
  TextField,
  Checkbox,
  Grid,
  InputLabel,
  FormControl,
  Select,
  MenuItem,
  Chip,
  FormControlLabel,
  Typography,
} from "@material-ui/core";
import NumberFormat from "react-number-format";
import RedeemIcon from "@material-ui/icons/Redeem";
import { makeStyles } from "@material-ui/core/styles";

function CostFormatCustom(props) {
  const { inputRef, onChange, ...other } = props;
  return (
    <NumberFormat
      {...other}
      getInputRef={inputRef}
      onValueChange={(values) => {
        onChange({
          target: {
            name: props.name,
            value: values.value,
          },
        });
      }}
      thousandSeparator
      isNumericString
      prefix="$"
    />
  );
}

function QuantityFormatCustom(props) {
  const { inputRef, onChange, ...other } = props;
  return (
    <NumberFormat
      {...other}
      getInputRef={inputRef}
      onValueChange={(values) => {
        onChange({
          target: {
            name: props.name,
            value: values.value,
          },
        });
      }}
      thousandSeparator
      isNumericString
    />
  );
}

const useStyles = makeStyles((theme) => ({
  root: {
    "&$checked": {
      color: "#388e3c",
    },
  },
  checked: {},
  paper: {
    marginTop: theme.spacing(8),
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  textField: {
    "& label.Mui-focused": {
      color: "white",
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: theme.palette.success.dark,
    },
    "& .MuiOutlinedInput-root": {
      "& fieldset": {
        borderColor: "white",
      },
      "&:hover fieldset": {
        borderColor: "white",
      },
      "&.Mui-focused fieldset": {
        borderColor: theme.palette.success.dark,
      },
    },
  },
  numericalField: {
    "& label.Mui-focused": {
      color: "white",
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: theme.palette.success.dark,
    },
    "& .MuiOutlinedInput-root": {
      "& fieldset": {
        borderColor: "white",
      },
      "&:hover fieldset": {
        borderColor: "white",
      },
      "&.Mui-focused fieldset": {
        borderColor: theme.palette.success.dark,
      },
    },
  },
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120,
    "& label.Mui-focused": {
      color: "white",
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: theme.palette.success.dark,
    },
    "& .MuiOutlinedInput-root": {
      "& fieldset": {
        borderColor: "white",
      },
      "&:hover fieldset": {
        borderColor: "white",
      },
      "&.Mui-focused fieldset": {
        borderColor: theme.palette.success.dark,
      },
    },
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.success.main,
  },
  form: {
    width: "75%", // Fix IE 11 issue.
    marginTop: theme.spacing(3),
  },
  submit: {
    marginTop: theme.spacing(0.5),
    color: "white",
    backgroundColor: theme.palette.success.main,
    "&:hover": {
      backgroundColor: theme.palette.success.light,
    },
  },
  chipBox: {
    display: "flex",
    justifyContent: "center",
    flexWrap: "wrap",
    listStyle: "none",
    padding: theme.spacing(0.5),
    margin: 0,
  },
  chip: {
    margin: theme.spacing(0.5),
  },
  input: {
    display: "none",
  },
  error: {
    textAlign: "center",
    color: theme.palette.success.main,
  },
}));

export default function ProductEditBody(props) {
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState("");
  const classes = useStyles();
  const [called, setCalled] = useState(false);
  const session = props.session;
  const productId = props.match.params.productId;
  const [product, setProduct] = useState(null);
  const onChange = (e) => {
    setProduct({
      ...product,
      [e.target.name]: e.target.value,
    });
  };
  const onChangeCheck = (e) => {
    setProduct({
      ...product,
      [e.target.name]: e.target.checked,
    });
  };
  const [agreement, setAgreement] = useState(false);
  const onAgreementCheck = (e) => {
    setAgreement(e.target.checked);
  };
  const [tag, setTag] = useState("");
  const onTagChange = (event) => {
    setTag(event.target.value);
  };
  const handleTagInsert = () => {
    if (!product.tags.find((t) => t.name === tag)) {
      setProduct({
        ...product,
        tags: [...product.tags, { name: tag }],
      });
    }
    setTag("");
  };
  const handleTagDelete = (tagToDelete) => () => {
    setProduct({
      ...product,
      tags: product.tags.filter((tag) => tag.name !== tagToDelete.name),
    });
  };
  const GenderChange = (gender) => {
    if (product.allowedGender.includes(gender)) {
      setProduct({
        ...product,
        allowedGender: product.allowedGender.filter((g) => g !== gender),
      });
    } else {
      setProduct({
        ...product,
        allowedGender: [...product.allowedGender, gender],
      });
    }
  };
  const [option, setOption] = useState("");
  const [openOption, setOpenOption] = useState(false);
  const onOptionChange = (e) => {
    setProduct({
      ...product,
      allowedJobs: [],
      bannedJobs: [],
    });
    setOption(e.target.value);
  };
  const onOptionClose = () => {
    setOpenOption(false);
  };
  const onOptionOpen = () => {
    setOpenOption(true);
  };
  const [job, setJob] = useState("");
  const onJobChange = (event) => {
    setJob(event.target.value);
  };
  const handleJobInsert = () => {
    if (option === "ALLOW") {
      if (!product.allowedJobs.find((j) => j.name === job)) {
        setProduct({
          ...product,
          allowedJobs: [...product.allowedJobs, { name: job }],
        });
      }
    } else if (option === "BAN") {
      if (!product.bannedJobs.find((j) => j.name === job)) {
        setProduct({
          ...product,
          bannedJobs: [...product.bannedJobs, { name: job }],
        });
      }
    }
    setJob("");
  };
  const handleJobDelete = (jobToDelete) => () => {
    if (option === "ALLOW") {
      setProduct({
        ...product,
        allowedJobs: product.allowedJobs.filter(
          (job) => job.name !== jobToDelete.name
        ),
      });
    } else if (option === "BAN") {
      setProduct({
        ...product,
        bannedJobs: product.bannedJobs.filter(
          (job) => job.name !== jobToDelete.name
        ),
      });
    }
  };
  const [profileImageData, setProfileImageData] = useState(null);
  const handleProfileImageUpload = (e) => {
    setProfileImageData(e.target.files[0]);
  };
  const dispatch = useDispatch();
  const loading = () => dispatch(sellerLoading());
  const done = () => dispatch(sellerDone());
  const GetProduct = async () => {
    loading();
    await axios
      .get("/seller/product/" + productId, {
        params: {
          getterz_session: session,
        },
      })
      .then(function (response) {
        if (response.data.resultCode === "OK") {
          setProduct(response.data.data);
          if (response.data.data.allowedJobs.length > 0) setOption("ALLOW");
          else if (response.data.data.bannedJobs.length > 0) setOption("BAN");
        }
        done();
      })
      .catch(function (error) {
        done();
      });
  };
  if (!called) {
    setCalled(true);
    GetProduct();
  }
  const Edit = async () => {
    if (!product.name || product.name === "") {
      setError("PLEASE CHECK PRODUCT NAME");
    } else if (parseInt(product.cost, 10) <= 0) {
      setError("PLEASE CHECK PRODUCT COST");
    } else if (parseInt(product.quantity, 10) <= 0) {
      setError("PLEASE CHECK PRODUCT QUANTITY");
    } else if (product.tags.length === 0) {
      setError("PLEASE INSERT PRODUCT TAGS");
    } else if (
      parseInt(product.allowedMinimumAge, 10) < 0 ||
      parseInt(product.allowedMaximumAge, 10) < 0 ||
      parseInt(product.allowedMinimumAge, 10) >
        parseInt(product.allowedMaximumAge, 10)
    ) {
      setError("PLEASE CHECK AGE REGULATION");
    } else if (product.allowedGender.length === 0) {
      setError("PLEASE CHECK GENDER REGULATION");
    } else if (
      product.allowedJobs.length === 0 &&
      product.bannedJobs.length === 0
    ) {
      setError("PLEASE INSERT REGULATION JOBS");
    } else if (product.descriptionLink === "") {
      setError("PLEASE CHECK DESCRIPTION LINK");
    } else if (!agreement) {
      setError("PLEASE CHECK THE CONSENT");
    } else {
      loading();
      await axios
        .put("/seller/product", {
          transactionTime: "",
          transactionType: "SELLER PRODUCT EDIT",
          data: {
            id: product.id,
            name: product.name,
            cost: parseInt(product.cost, 10),
            tags: product.tags.map((t) => t.name),
            quantity: parseInt(product.quantity, 10),
            allowedGender: product.allowedGender,
            allowedMinimumAge: parseInt(product.allowedMinimumAge, 10),
            allowedMaximumAge: parseInt(product.allowedMaximumAge, 10),
            allowedJob:
              product.allowedJobs.length > 0
                ? product.allowedJobs.map((j) => j.name)
                : null,
            bannedJob:
              product.bannedJobs.length > 0
                ? product.bannedJobs.map((j) => j.name)
                : null,
            exposeToNoQualify: product.exposeToNoQualify,
            descriptionLink: product.descriptionLink,
          },
          session: session,
        })
        .then(function (response) {
          if (response.data.resultCode === "OK") {
            if (profileImageData) {
              const profileFormData = new FormData();
              profileFormData.append("profileImage", profileImageData);
              profileFormData.append(
                "fileName",
                product.profileImageName.split(".")[0]
              );
              axios
                .post("/seller/product/profile_image_upload", profileFormData, {
                  headers: { "Content-Type": "multipart/form-data" },
                })
                .then(function (response) {
                  if (response.data.resultCode === "OK") {
                    setSuccess(true);
                  } else if (response.data.resultCode === "ERROR") {
                    setError(response.data.description);
                  }
                });
            } else {
              setSuccess(true);
            }
          } else if (response.data.resultCode === "ERROR") {
            setError(response.data.description);
          }
          done();
        })
        .catch(function (error) {
          done();
        });
    }
  };
  return success ? (
    <Typography className={classes.error} variant="h3">
      {"PRODUCT REGISTER SUCCESS"}
    </Typography>
  ) : product ? (
    <div className={classes.paper}>
      <Avatar className={classes.avatar}>
        <RedeemIcon />
      </Avatar>
      <Typography component="h1" variant="h5">
        Register Product
      </Typography>
      <form className={classes.form} noValidate>
        <Grid container spacing={2} justify="center" alignItems="center">
          <Grid item xs={6}>
            <TextField
              className={classes.textField}
              name="name"
              variant="outlined"
              required
              fullWidth
              id="name"
              value={product.name}
              onChange={onChange}
              label="Product name"
              autoFocus
            />
          </Grid>
          <Grid item xs={3}>
            <TextField
              className={classes.numericalField}
              InputLabelProps={{ style: { color: "white" } }}
              label="Cost"
              name="cost"
              id="cost"
              value={product.cost}
              onChange={onChange}
              InputProps={{ inputComponent: CostFormatCustom }}
            />
          </Grid>
          <Grid item xs={3}>
            <TextField
              className={classes.numericalField}
              InputLabelProps={{ style: { color: "white" } }}
              label="Quantity"
              name="quantity"
              id="quantity"
              value={product.quantity}
              onChange={onChange}
              InputProps={{ inputComponent: QuantityFormatCustom }}
            />
          </Grid>
          <Grid item xs={4}>
            <TextField
              className={classes.textField}
              value={tag}
              onChange={onTagChange}
              name="tag"
              variant="outlined"
              fullWidth
              id="tag"
              label="Insert tag of product"
              onKeyUp={(e) => {
                if (e.key === "Enter") {
                  handleTagInsert(tag);
                }
              }}
            />
          </Grid>
          <Grid item xs={8} className={classes.chipBox}>
            {product.tags.length > 0 ? (
              product.tags.map((t) => {
                return (
                  <li>
                    <Chip
                      label={t.name}
                      onDelete={handleTagDelete(t)}
                      className={classes.chip}
                    />
                  </li>
                );
              })
            ) : (
              <Typography variant="body2" color="textSecondary">
                Tag List
              </Typography>
            )}
          </Grid>
          <Grid item xs={3}>
            <TextField
              className={classes.numericalField}
              InputLabelProps={{ style: { color: "white" } }}
              label="Minimum age"
              name="allowedMinimumAge"
              id="allowedMinimumAge"
              value={product.allowedMinimumAge}
              onChange={onChange}
              InputProps={{ inputComponent: QuantityFormatCustom }}
            />
          </Grid>
          <Grid item xs={3}>
            <TextField
              className={classes.numericalField}
              InputLabelProps={{ style: { color: "white" } }}
              label="Maximum age"
              name="allowedMaximumAge"
              id="allowedMaximumAge"
              value={product.allowedMaximumAge}
              onChange={onChange}
              InputProps={{ inputComponent: QuantityFormatCustom }}
            />
          </Grid>
          <Grid item xs={1}>
            <FormControlLabel
              control={
                <Checkbox
                  checked={product.allowedGender.includes("MALE_STRAIGHT")}
                  onClick={() => GenderChange("MALE_STRAIGHT")}
                  name="allowMS"
                  id="allowMS"
                  classes={{
                    root: classes.root,
                    checked: classes.checked,
                  }}
                />
              }
              label="Male straight"
            />
          </Grid>
          <Grid item xs={1}>
            <FormControlLabel
              control={
                <Checkbox
                  checked={product.allowedGender.includes("MALE_GAY")}
                  onClick={() => GenderChange("MALE_GAY")}
                  name="allowMG"
                  id="allowMG"
                  classes={{
                    root: classes.root,
                    checked: classes.checked,
                  }}
                />
              }
              label="Male gay"
            />
          </Grid>
          <Grid item xs={1}>
            <FormControlLabel
              control={
                <Checkbox
                  checked={product.allowedGender.includes("MALE_BISEXUAL")}
                  onClick={() => GenderChange("MALE_BISEXUAL")}
                  name="allowMB"
                  id="allowMB"
                  classes={{
                    root: classes.root,
                    checked: classes.checked,
                  }}
                />
              }
              label="Male bisexual"
            />
          </Grid>
          <Grid item xs={1}>
            <FormControlLabel
              control={
                <Checkbox
                  checked={product.allowedGender.includes("FEMALE_STRAIGHT")}
                  onClick={() => GenderChange("FEMALE_STRAIGHT")}
                  name="allowFS"
                  id="allowFS"
                  classes={{
                    root: classes.root,
                    checked: classes.checked,
                  }}
                />
              }
              label="Female straight"
            />
          </Grid>
          <Grid item xs={1}>
            <FormControlLabel
              control={
                <Checkbox
                  checked={product.allowedGender.includes("FEMALE_GAY")}
                  onClick={() => GenderChange("FEMALE_GAY")}
                  name="allowFG"
                  id="allowFG"
                  classes={{
                    root: classes.root,
                    checked: classes.checked,
                  }}
                />
              }
              label="Female gay"
            />
          </Grid>
          <Grid item xs={1}>
            <FormControlLabel
              control={
                <Checkbox
                  checked={product.allowedGender.includes("FEMALE_BISEXUAL")}
                  onClick={() => GenderChange("FEMALE_BISEXUAL")}
                  name="allowFB"
                  id="allowFB"
                  classes={{
                    root: classes.root,
                    checked: classes.checked,
                  }}
                />
              }
              label="Female bisexual"
            />
          </Grid>
          <Grid item xs={2}>
            <FormControl className={classes.formControl}>
              <InputLabel id="option-select-label" style={{ color: "white" }}>
                {option}
              </InputLabel>
              <Select
                labelId="option-select-label"
                label="Option"
                id="optionSelect"
                open={openOption}
                onClose={onOptionClose}
                onOpen={onOptionOpen}
                value={option}
                onChange={onOptionChange}
              >
                <MenuItem value={"ALLOW"}>Allow</MenuItem>
                <MenuItem value={"BAN"}>Ban</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={4}>
            <TextField
              className={classes.textField}
              value={job}
              onChange={onJobChange}
              name="job"
              variant="outlined"
              fullWidth
              id="job"
              label="these job(s)"
              onKeyUp={(e) => {
                if (e.key === "Enter") {
                  handleJobInsert(job);
                }
              }}
            />
          </Grid>
          <Grid item xs={6} className={classes.chipBox}>
            {product.allowedJobs.length > 0 ? (
              product.allowedJobs.map((j) => {
                return (
                  <li>
                    <Chip
                      label={j.name}
                      onDelete={handleJobDelete(j)}
                      className={classes.chip}
                    />
                  </li>
                );
              })
            ) : product.bannedJobs.length > 0 ? (
              product.bannedJobs.map((j) => {
                return (
                  <li>
                    <Chip
                      label={j.name}
                      onDelete={handleJobDelete(j)}
                      className={classes.chip}
                    />
                  </li>
                );
              })
            ) : (
              <Typography variant="body2" color="textSecondary">
                Job List
              </Typography>
            )}
          </Grid>
          <Grid container item xs={3} direction="column" alignItems="center">
            <input
              accept="image/*"
              className={classes.input}
              id="profileImage"
              single
              type="file"
              onChange={handleProfileImageUpload}
            />
            <label htmlFor="profileImage">
              <Button
                className={classes.submit}
                fullWidth
                variant="contained"
                component="span"
                style={{ textTransform: "none" }}
              >
                {"Upload a product's profile image"}
              </Button>
            </label>
          </Grid>
          <Grid container item xs={3} direction="column" alignItems="center">
            <TextField
              className={classes.textField}
              name="descriptionLink"
              variant="outlined"
              required
              fullWidth
              id="descriptionLink"
              value={product.descriptionLink}
              onChange={onChange}
              label="Description link"
            />
          </Grid>
          <Grid item xs={3}>
            <FormControlLabel
              control={
                <Checkbox
                  checked={product.exposeToNoQualify}
                  onChange={onChangeCheck}
                  name="exposeToNoQualify"
                  id="exposeToNoQualify"
                  classes={{
                    root: classes.root,
                    checked: classes.checked,
                  }}
                />
              }
              label="Expose to none qualified buyers?"
            />
          </Grid>
          <Grid item xs={3}>
            <FormControlLabel
              control={
                <Checkbox
                  value={agreement}
                  onChange={onAgreementCheck}
                  name="agreement"
                  id="agreement"
                  classes={{
                    root: classes.root,
                    checked: classes.checked,
                  }}
                />
              }
              label="I consent to register this product on Getterz."
            />
          </Grid>
        </Grid>
        {error === "" ? (
          <></>
        ) : (
          <Typography className={classes.error}>{error}</Typography>
        )}
        <Button
          fullWidth
          variant="contained"
          className={classes.submit}
          onClick={Edit}
        >
          Edit
        </Button>
      </form>
    </div>
  ) : (
    <></>
  );
}
