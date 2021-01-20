import React, { useState } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { sellerLoading, sellerDone } from "../info/SellerInfo";
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

export default function ProductRegisterBody(props) {
  const classes = useStyles();
  const [success, setSuccess] = useState(false);
  const [inputs, setInputs] = useState({
    name: "",
    cost: null,
    tags: [],
    quantity: null,
    allowMS: false,
    allowMG: false,
    allowMB: false,
    allowFS: false,
    allowFG: false,
    allowFB: false,
    allowedMinimumAge: null,
    allowedMaximumAge: null,
    option: "ALLOW",
    jobs: [],
    exposeToNoQualify: false,
    descriptionLink: "",
    agreement: false,
    error: "",
  });
  const {
    name,
    cost,
    tags,
    quantity,
    allowMS,
    allowMG,
    allowMB,
    allowFS,
    allowFG,
    allowFB,
    allowedMinimumAge,
    allowedMaximumAge,
    option,
    jobs,
    exposeToNoQualify,
    descriptionLink,
    agreement,
    error,
  } = inputs;
  const onChange = (e) => {
    setInputs({
      ...inputs,
      [e.target.name]: e.target.value,
    });
  };
  const onChangeCheck = (e) => {
    setInputs({
      ...inputs,
      [e.target.name]: e.target.checked,
    });
  };
  const setError = (message) => {
    setInputs({
      ...inputs,
      error: message,
    });
  };
  const [openOption, setOpenOption] = useState(false);
  const onOptionChange = (e) => {
    setInputs({
      ...inputs,
      option: e.target.value,
    });
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
    if (!jobs.find((j) => j.name === job)) {
      setInputs({
        ...inputs,
        jobs: [...jobs, { name: job }],
      });
    }
    setJob("");
  };
  const handleJobDelete = (jobToDelete) => () => {
    setInputs({
      ...inputs,
      jobs: jobs.filter((job) => job.name !== jobToDelete.name),
    });
  };
  const [tag, setTag] = useState("");
  const onTagChange = (event) => {
    setTag(event.target.value);
  };
  const handleTagInsert = () => {
    if (!tags.find((t) => t.name === tag)) {
      setInputs({
        ...inputs,
        tags: [...tags, { name: tag }],
      });
    }
    setTag("");
  };
  const handleTagDelete = (tagToDelete) => () => {
    setInputs({
      ...inputs,
      tags: tags.filter((tag) => tag.name !== tagToDelete.name),
    });
  };
  const [profileImageData, setProfileImageData] = useState(null);
  const handleProfileImageUpload = (e) => {
    setProfileImageData(e.target.files[0]);
  };
  const dispatch = useDispatch();
  const loading = () => dispatch(sellerLoading());
  const done = () => dispatch(sellerDone());
  const Register = async () => {
    console.log(option);
    console.log(tags);
    console.log(jobs);
    if (!name || name === "") {
      setError("PLEASE CHECK PRODUCT NAME");
    } else if (parseInt(cost, 10) <= 0) {
      setError("PLEASE CHECK PRODUCT COST");
    } else if (parseInt(quantity, 10) <= 0) {
      setError("PLEASE CHECK PRODUCT QUANTITY");
    } else if (tags.length === 0) {
      setError("PLEASE INSERT PRODUCT TAGS");
    } else if (
      parseInt(allowedMinimumAge, 10) < 0 ||
      parseInt(allowedMaximumAge, 10) < 0 ||
      parseInt(allowedMinimumAge, 10) > parseInt(allowedMaximumAge, 10)
    ) {
      setError("PLEASE CHECK AGE REGULATION");
    } else if (
      !allowMS &&
      !allowMG &&
      !allowMB &&
      !allowFS &&
      !allowFG &&
      !allowFB
    ) {
      setError("PLEASE CHECK GENDER REGULATION");
    } else if (jobs.length === 0) {
      setError("PLEASE INSERT REGULATION JOBS");
    } else if (!profileImageData) {
      setError("PLEASE SELECT PROFILE IMAGE");
    } else if (descriptionLink === "") {
      setError("PLEASE CHECK DESCRIPTION LINK");
    } else if (!agreement) {
      setError("PLEASE CHECK THE CONSENT");
    } else {
      loading();
      let allowedGenders = [];
      if (allowMS) allowedGenders.push("MALE_STRAIGHT");
      if (allowMG) allowedGenders.push("MALE_GAY");
      if (allowMB) allowedGenders.push("MALE_BISEXUAL");
      if (allowFS) allowedGenders.push("FEMALE_STRAIGHT");
      if (allowFG) allowedGenders.push("FEMALE_GAY");
      if (allowFB) allowedGenders.push("FEMALE_BISEXUAL");
      await axios
        .post("/seller/product", {
          transactionTime: "",
          transactionType: "SELLER PRODUCT REGISTER",
          data: {
            name: name,
            cost: parseInt(cost, 10),
            tags: tags.map((t) => t.name),
            quantity: parseInt(quantity, 10),
            allowedGender: allowedGenders,
            allowedMinimumAge: parseInt(allowedMinimumAge, 10),
            allowedMaximumAge: parseInt(allowedMaximumAge, 10),
            allowedJob: option === "ALLOW" ? jobs.map((j) => j.name) : null,
            bannedJob: option === "BAN" ? jobs.map((j) => j.name) : null,
            exposeToNoQualify: exposeToNoQualify,
            descriptionLink: descriptionLink,
          },
          session: props.session,
        })
        .then(function (response) {
          if (response.data.resultCode === "OK") {
            const profileFormData = new FormData();
            profileFormData.append("profileImage", profileImageData);
            profileFormData.append("fileName", response.data.description);
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
  ) : (
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
              value={name}
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
              value={cost}
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
              value={quantity}
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
            {tags.length > 0 ? (
              tags.map((t) => {
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
              value={allowedMinimumAge}
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
              value={allowedMaximumAge}
              onChange={onChange}
              InputProps={{ inputComponent: QuantityFormatCustom }}
            />
          </Grid>
          <Grid item xs={1}>
            <FormControlLabel
              control={
                <Checkbox
                  value={allowMS}
                  onChange={onChangeCheck}
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
                  value={allowMG}
                  onChange={onChangeCheck}
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
                  value={allowMB}
                  onChange={onChangeCheck}
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
                  value={allowFS}
                  onChange={onChangeCheck}
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
                  value={allowFG}
                  onChange={onChangeCheck}
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
                  value={allowFB}
                  onChange={onChangeCheck}
                  name="allowFB]"
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
                I would
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
            {jobs.length > 0 ? (
              jobs.map((j) => {
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
              value={descriptionLink}
              onChange={onChange}
              label="Description link"
            />
          </Grid>
          <Grid item xs={3}>
            <FormControlLabel
              control={
                <Checkbox
                  value={exposeToNoQualify}
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
                  onChange={onChangeCheck}
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
          onClick={Register}
        >
          Register
        </Button>
      </form>
    </div>
  );
}
