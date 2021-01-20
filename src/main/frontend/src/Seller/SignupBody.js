import React, { useState, useEffect, useRef } from "react";
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
  FormControlLabel,
  Typography,
} from "@material-ui/core";
import Autocomplete from "@material-ui/lab/Autocomplete";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import { makeStyles } from "@material-ui/core/styles";
import MuiPhoneNumber from "material-ui-phone-number";
import LocationOnIcon from "@material-ui/icons/LocationOn";
import parse from "autosuggest-highlight/parse";
import throttle from "lodash/throttle";

function loadScript(src, position, id) {
  if (!position) {
    return;
  }
  const script = document.createElement("script");
  script.setAttribute("async", "");
  script.setAttribute("id", id);
  script.src = src;
  position.appendChild(script);
}

const autocompleteService = { current: null };

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
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
    margin: theme.spacing(3, 0, 2),
    color: "white",
    backgroundColor: theme.palette.success.main,
    "&:hover": {
      backgroundColor: theme.palette.success.light,
    },
  },
  uploadButton: {
    color: "white",
    backgroundColor: theme.palette.success.main,
    "&:hover": {
      backgroundColor: theme.palette.success.light,
    },
  },
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120,
    "& label.Mui-focused": {
      color: theme.palette.success.dark,
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: theme.palette.success.dark,
    },
    "& .MuiOutlinedInput-root": {
      "&.Mui-focused fieldset": {
        borderColor: theme.palette.success.dark,
      },
    },
  },
  container: {
    display: "flex",
    flexWrap: "wrap",
  },
  textField: {
    marginLeft: theme.spacing(1),
    marginRight: theme.spacing(1),
    width: 200,
    "& label.Mui-focused": {
      color: theme.palette.success.dark,
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: theme.palette.success.dark,
    },
    "& .MuiOutlinedInput-root": {
      "&.Mui-focused fieldset": {
        borderColor: theme.palette.success.dark,
      },
    },
  },
  inputField: {
    "& label.Mui-focused": {
      color: theme.palette.success.dark,
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: theme.palette.success.dark,
    },
    "& .MuiOutlinedInput-root": {
      "&.Mui-focused fieldset": {
        borderColor: theme.palette.success.dark,
      },
    },
  },
  icon: {
    color: theme.palette.text.secondary,
    marginRight: theme.spacing(2),
  },
  input: {
    display: "none",
  },
  error: {
    textAlign: "center",
    color: theme.palette.success.main,
  },
  root: {
    "&$checked": {
      color: "#388e3c",
    },
  },
  checked: {},
}));

export default function SignupBody() {
  const classes = useStyles();
  const [success, setSuccess] = useState(false);
  const [inputs, setInputs] = useState({
    name: "",
    emailAddress: "",
    password: "",
    passwordCheck: "",
    emergencyPassword: "",
    emergencyPasswordCheck: "",
    gender: "",
    dateOfBirth: "",
    cellNumber: "",
    addressDetail: "",
    agreement: false,
    error: "",
  });
  const {
    name,
    emailAddress,
    password,
    passwordCheck,
    emergencyPassword,
    emergencyPasswordCheck,
    gender,
    dateOfBirth,
    cellNumber,
    addressDetail,
    agreement,
    error,
  } = inputs;
  const onChange = (e) => {
    setInputs({
      ...inputs,
      [e.target.name]: e.target.value,
    });
  };
  const onAgreementChange = (e) => {
    setInputs({ ...inputs, agreement: e.target.checked });
  };
  const [openGender, setOpenGender] = useState(false);
  const onGenderChange = (e) => {
    setInputs({
      ...inputs,
      gender: e.target.value,
    });
  };
  const onGenderClose = () => {
    setOpenGender(false);
  };
  const onGenderOpen = () => {
    setOpenGender(true);
  };
  const handlePhoneChange = (value) => {
    if (value) {
      setInputs({
        ...inputs,
        cellNumber: value,
      });
    }
  };
  const [addressValue, setAddressValue] = useState(null);
  const [addressInputValue, setAddressInputValue] = useState("");
  const [addressOptions, setAddressOptions] = useState([]);
  const addressLoaded = useRef(false);
  if (typeof window !== "undefined" && !addressLoaded.current) {
    if (!document.querySelector("#google-maps")) {
      loadScript(
        "https://maps.googleapis.com/maps/api/js?key=AIzaSyCoTwBdkHB8bkZ7PNzRdnAVSG8OY7CXQUs&libraries=places",
        document.querySelector("head"),
        "google-maps"
      );
    }
    addressLoaded.current = true;
  }
  const fetchAddress = React.useMemo(
    () =>
      throttle((request, callback) => {
        autocompleteService.current.getPlacePredictions(request, callback);
      }, 200),
    []
  );
  useEffect(() => {
    let active = true;
    if (!autocompleteService.current && window.google) {
      autocompleteService.current = new window.google.maps.places.AutocompleteService();
    }
    if (!autocompleteService.current) {
      return undefined;
    }
    if (addressInputValue === "") {
      setAddressOptions(addressValue ? [addressValue] : []);
      return undefined;
    }
    fetchAddress({ input: addressInputValue }, (results) => {
      if (active) {
        let newOptions = [];
        if (addressValue) {
          newOptions = [addressValue];
        }
        if (results) {
          newOptions = [...newOptions, ...results];
        }
        setAddressOptions(newOptions);
      }
    });
    return () => {
      active = false;
    };
  }, [addressValue, addressInputValue, fetchAddress]);
  const [sellerImageData, setSellerImageData] = useState(null);
  const handleSellerImageUpload = (e) => {
    setSellerImageData(e.target.files[0]);
  };
  const [profileImageData, setProfileImageData] = useState(null);
  const handleProfileImageUpload = (e) => {
    setProfileImageData(e.target.files[0]);
  };
  const dispatch = useDispatch();
  const loading = () => dispatch(sellerLoading());
  const done = () => dispatch(sellerDone());
  const Signup = (inputs, setInputs, addressValue, setAddressValue) => {
    const setError = (e) => {
      setInputs({
        ...inputs,
        error: e,
      });
    };
    if (!inputs.agreement) {
      setError("PLEASE CHECK THE CONSENT");
    } else if (inputs.password !== inputs.passwordCheck) {
      setError("PASSWORD MISMATCH");
    } else if (inputs.emergencyPassword !== inputs.emergencyPasswordCheck) {
      setError("EMERGENCY PASSWORD MISMATCH");
    } else if (inputs.password === inputs.emergencyPassword) {
      setError("DUPLICATE PASSWORD");
    } else if (!addressValue) {
      setError("INVALID ADDRESS");
    } else if (!sellerImageData) {
      setError("PLEASE UPLOAD SELLER VERIFYING PHOTO");
    } else {
      new window.google.maps.Geocoder().geocode(
        { placeId: addressValue.place_id },
        async (results, status) => {
          if (status === "OK") {
            loading();
            var sellerVerifyImageName = null;
            var profileImageName = null;
            const sellerFormData = new FormData();
            const profileFormData = new FormData();
            sellerFormData.append("verifyImage", sellerImageData);
            sellerFormData.append(
              "fileName",
              inputs.emailAddress.replace("@", "").replace(".", "")
            );
            profileFormData.append("profileImage", profileImageData);
            profileFormData.append(
              "fileName",
              inputs.emailAddress.replace("@", "").replace(".", "")
            );
            await axios
              .post("/seller/verify_image_upload", sellerFormData, {
                headers: { "Content-Type": "multipart/form-data" },
              })
              .then(function (response) {
                if (response.data.resultCode === "OK") {
                  sellerVerifyImageName = response.data.description;
                } else if (response.data.resultCode === "ERROR") {
                  setError(response.data.description);
                }
              });
            await axios
              .post("/seller/profile_image_upload", profileFormData, {
                headers: { "Content-Type": "multipart/form-data" },
              })
              .then(function (response) {
                if (response.data.resultCode === "OK") {
                  profileImageName = response.data.description;
                }
                if (response.data.resultCode === "ERROR") {
                  setError(response.data.description);
                }
              });
            if (sellerVerifyImageName) {
              await axios
                .post("/seller/signup", {
                  transactionTime: "",
                  transactionType: "SELLER SIGNUP",
                  data: {
                    name: inputs.name,
                    password: inputs.password,
                    emergencyPassword: inputs.emergencyPassword,
                    gender: inputs.gender,
                    dateOfBirth: inputs.dateOfBirth,
                    emailAddress: inputs.emailAddress,
                    cellNumber: inputs.cellNumber,
                    latitude: results[0].geometry.location.lat(),
                    longitude: results[0].geometry.location.lng(),
                    address:
                      addressValue.description + " " + inputs.addressDetail,
                    verifyImageName: sellerVerifyImageName,
                    profileImageName: profileImageName,
                  },
                })
                .then(function (response) {
                  if (response.data.resultCode === "OK") {
                    setSuccess(true);
                  } else if (response.data.resultCode === "ERROR") {
                    setError(response.data.description);
                  }
                  done();
                })
                .catch(function (error) {
                  done();
                });
            }
          }
        }
      );
    }
  };
  return success ? (
    <Typography className={classes.error} variant="h3">
      {
        "SELLER CREATE SUCCESS PLEASE CHECK YOUR EMAIL AND PROCEED CONFIRMATION PROCESS"
      }
    </Typography>
  ) : (
    <div className={classes.paper}>
      <Avatar className={classes.avatar}>
        <LockOutlinedIcon />
      </Avatar>
      <Typography component="h1" variant="h5">
        Sign up
      </Typography>
      <form className={classes.form} noValidate>
        <Grid container spacing={2}>
          <Grid item xs={6} sm={6}>
            <TextField
              className={classes.inputField}
              name="name"
              variant="outlined"
              required
              fullWidth
              id="name"
              value={name}
              onChange={onChange}
              label="Full Name"
              autoFocus
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              className={classes.inputField}
              variant="outlined"
              required
              fullWidth
              id="emailAddress"
              value={emailAddress}
              onChange={onChange}
              label="Email Address"
              name="emailAddress"
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              className={classes.inputField}
              variant="outlined"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              value={password}
              onChange={onChange}
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              className={classes.inputField}
              variant="outlined"
              required
              fullWidth
              name="passwordCheck"
              label="Check Password"
              type="password"
              id="passwordCheck"
              value={passwordCheck}
              onChange={onChange}
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              className={classes.inputField}
              variant="outlined"
              required
              fullWidth
              name="emergencyPassword"
              label="Emergency Password"
              type="password"
              id="emergencyPassword"
              value={emergencyPassword}
              onChange={onChange}
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              className={classes.inputField}
              variant="outlined"
              required
              fullWidth
              name="emergencyPasswordCheck"
              label="Check Emergency Password"
              type="password"
              id="emergencyPasswordCheck"
              value={emergencyPasswordCheck}
              onChange={onChange}
            />
          </Grid>
          <Grid item xs={2}>
            <FormControl className={classes.formControl}>
              <InputLabel id="gender-select-label">Gender</InputLabel>
              <Select
                labelId="gender-select-label"
                label="Gender"
                id="genderSelect"
                open={openGender}
                onClose={onGenderClose}
                onOpen={onGenderOpen}
                value={gender}
                onChange={onGenderChange}
              >
                <MenuItem value={"MALE_STRAIGHT"}>MALE_STRAIGHT</MenuItem>
                <MenuItem value={"MALE_GAY"}>MALE_GAY</MenuItem>
                <MenuItem value={"MALE_BISEXUAL"}>MALE_BISEXUAL</MenuItem>
                <MenuItem value={"FEMALE_STRAIGHT"}>FEMALE_STRAIGHT</MenuItem>
                <MenuItem value={"FEMALE_GAY"}>FEMALE_GAY</MenuItem>
                <MenuItem value={"FEMALE_BISEXUAL"}>FEMALE_BISEXUAL</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={2}>
            <form className={classes.container} noValidate>
              <TextField
                value={dateOfBirth}
                onChange={onChange}
                id="dateOfBirth"
                name="dateOfBirth"
                label="Date of birth"
                type="date"
                className={classes.textField}
                InputLabelProps={{
                  shrink: true,
                }}
              />
            </form>
          </Grid>
          <Grid item xs={2}>
            <MuiPhoneNumber
              className={classes.inputField}
              name="cellNumber"
              value={cellNumber}
              onChange={handlePhoneChange}
              label="Cell Number"
              data-cy="user-phone"
              defaultCountry={"us"}
            />
          </Grid>
          <Grid item xs={4}>
            <Autocomplete
              className={classes.inputField}
              id="googleMapAddress"
              style={{ width: 300 }}
              getOptionLabel={(option) =>
                typeof option === "string" ? option : option.description
              }
              filterOptions={(x) => x}
              options={addressOptions}
              autoComplete
              includeInputInList
              filterSelectedOptions
              value={addressValue}
              onChange={(event, newValue) => {
                setAddressOptions(
                  newValue ? [newValue, ...addressOptions] : addressOptions
                );
                setAddressValue(newValue);
              }}
              onInputChange={(event, newInputValue) => {
                setAddressInputValue(newInputValue);
              }}
              renderInput={(params) => (
                <TextField
                  {...params}
                  label="Add a location"
                  variant="outlined"
                  fullWidth
                />
              )}
              renderOption={(option) => {
                const matches =
                  option.structured_formatting.main_text_matched_substrings;
                const parts = parse(
                  option.structured_formatting.main_text,
                  matches.map((match) => [
                    match.offset,
                    match.offset + match.length,
                  ])
                );
                return (
                  <Grid container alignItems="center">
                    <Grid item>
                      <LocationOnIcon className={classes.icon} />
                    </Grid>
                    <Grid item xs>
                      {parts.map((part, index) => (
                        <span
                          key={index}
                          style={{ fontWeight: part.highlight ? 700 : 400 }}
                        >
                          {part.text}
                        </span>
                      ))}

                      <Typography variant="body2" color="textSecondary">
                        {option.structured_formatting.secondary_text}
                      </Typography>
                    </Grid>
                  </Grid>
                );
              }}
            />
          </Grid>
          <Grid item xs={2}>
            <TextField
              className={classes.inputField}
              variant="outlined"
              required
              fullWidth
              id="addressDetail"
              label="Address Detail"
              name="addressDetail"
              value={addressDetail}
              onChange={onChange}
            />
          </Grid>
          <Grid container item xs={3} direction="column" alignItems="center">
            <input
              accept="image/*"
              className={classes.input}
              id="verificationImage"
              single
              type="file"
              onChange={handleSellerImageUpload}
            />
            <label htmlFor="verificationImage">
              <Button
                className={classes.uploadButton}
                variant="contained"
                color="primary"
                component="span"
                style={{ textTransform: "none" }}
              >
                {"Upload a seller verifying photo"}
              </Button>
            </label>
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
                className={classes.uploadButton}
                fullWidth
                variant="contained"
                color="primary"
                component="span"
                style={{ textTransform: "none" }}
              >
                {"Upload a profile photo"}
              </Button>
            </label>
          </Grid>
          <Grid item xs={6}>
            <FormControlLabel
              control={
                <Checkbox
                  value={agreement}
                  onChange={onAgreementChange}
                  name="agreement"
                  id="agreement"
                  classes={{
                    root: classes.root,
                    checked: classes.checked,
                  }}
                />
              }
              label="I consent to the collection and use of my personal information."
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
          onClick={() =>
            Signup(inputs, setInputs, addressValue, setAddressValue)
          }
        >
          Sign Up
        </Button>
      </form>
    </div>
  );
}
