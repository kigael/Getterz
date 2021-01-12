import React, { useState } from "react";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";
import Grid from "@material-ui/core/Grid";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import MenuItem from "@material-ui/core/MenuItem";
import Select from "@material-ui/core/Select";
import InputLabel from "@material-ui/core/InputLabel";
import FormControl from "@material-ui/core/FormControl";
import MuiPhoneNumber from "material-ui-phone-number";
import Autocomplete from "@material-ui/lab/Autocomplete";
import LocationOnIcon from "@material-ui/icons/LocationOn";
import parse from "autosuggest-highlight/parse";
import throttle from "lodash/throttle";
import Chip from "@material-ui/core/Chip";
import OutlinedInput from "@material-ui/core/OutlinedInput";
import InputAdornment from "@material-ui/core/InputAdornment";

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
    backgroundColor: theme.palette.primary.main,
  },
  form: {
    width: "50%", // Fix IE 11 issue.
    marginTop: theme.spacing(3),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120,
  },
  container: {
    display: "flex",
    flexWrap: "wrap",
  },
  textField: {
    marginLeft: theme.spacing(1),
    marginRight: theme.spacing(1),
    width: 200,
  },
  icon: {
    color: theme.palette.text.secondary,
    marginRight: theme.spacing(2),
  },
  jobBox: {
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
  margin: {
    margin: theme.spacing(1),
  },
  input: {
    display: "none",
  },
}));

export default function SignupBody() {
  const classes = useStyles();
  const [gender, setGender] = React.useState("");
  const [openGender, setOpenGender] = React.useState(false);
  const onGenderChange = (event) => {
    setGender(event.target.value);
  };
  const onGenderClose = () => {
    setOpenGender(false);
  };
  const onGenderOpen = () => {
    setOpenGender(true);
  };
  const [addressValue, setAddressValue] = React.useState(null);
  const [addressInputValue, setAddressInputValue] = React.useState("");
  const [addressOptions, setAddressOptions] = React.useState([]);
  const addressLoaded = React.useRef(false);
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
  const addressFetch = React.useMemo(
    () =>
      throttle((request, callback) => {
        autocompleteService.current.getPlacePredictions(request, callback);
      }, 200),
    []
  );

  React.useEffect(() => {
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
    addressFetch({ input: addressInputValue }, (results) => {
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
  }, [addressValue, addressInputValue, addressFetch]);
  const [jobList, setJobList] = useState([]);
  const handleJobInsert = (jobToInsert) => {
    if (!jobList.find((j) => j.title === jobToInsert)) {
      setJobList((jobList) => [...jobList, { title: jobToInsert }]);
    }
    setJob("");
  };
  const handleJobDelete = (jobToDelete) => () => {
    setJobList((jobs) => jobs.filter((job) => job.title !== jobToDelete.title));
  };
  const [job, setJob] = useState("");
  const onjobChange = (event) => {
    setJob(event.target.value);
  };
  return (
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
              name="fullName"
              variant="outlined"
              required
              fullWidth
              id="fullName"
              label="Full Name"
              autoFocus
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              variant="outlined"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              variant="outlined"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              variant="outlined"
              required
              fullWidth
              name="checkPassword"
              label="Check Password"
              type="password"
              id="password"
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              variant="outlined"
              required
              fullWidth
              name="emergencyPassword"
              label="Emergency Password"
              type="password"
              id="password"
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              variant="outlined"
              required
              fullWidth
              name="checkEmergencyPassword"
              label="Check Emergency Password"
              type="password"
              id="password"
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
                id="dateOfBirth"
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
              name="phone"
              label="Phone Number"
              data-cy="user-phone"
              defaultCountry={"us"}
            />
          </Grid>
          <Grid item xs={4}>
            <Autocomplete
              id="google-map-demo"
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
                  label="Address"
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
                    <Grid iteam>
                      <LocationOnIcon className={classes.icon} />
                    </Grid>
                    <Grid item>
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
              variant="outlined"
              required
              fullWidth
              id="addressDetail"
              label="Address Detail"
              name="addressDetail"
            />
          </Grid>
          <Grid item xs={4} className={classes.jobBox}>
            {jobList.length > 0 ? (
              jobList.map((j) => {
                return (
                  <li>
                    <Chip
                      label={j.title}
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
          <Grid item xs={2}>
            <TextField
              value={job}
              onChange={onjobChange}
              name="jobInsert"
              variant="outlined"
              fullWidth
              id="jobInsert"
              label="Insert your job"
              onKeyUp={(e) => {
                if (e.key === "Enter") {
                  console.log("hello");
                  handleJobInsert(job);
                }
              }}
            />
          </Grid>
          <Grid item xs={3}>
            <FormControl className={classes.margin} variant="outlined">
              <InputLabel htmlFor="annualIncome">Annual Income</InputLabel>
              <OutlinedInput
                id="annualIncome"
                labelWidth={90}
                startAdornment={
                  <InputAdornment position="start">$</InputAdornment>
                }
              />
            </FormControl>
          </Grid>
          <Grid item xs={3}>
            <TextField
              name="bitCoinWallet"
              variant="outlined"
              fullWidth
              id="bitCoinWallet"
              label="Bitcoin Wallet Address"
            />
          </Grid>
          <Grid container item xs={6} direction="column" alignItems="center">
            <input
              accept="image/*"
              className={classes.input}
              id="verificationFile"
              multiple
              type="file"
            />
            <label htmlFor="verificationFile">
              <Button
                variant="contained"
                color="primary"
                component="span"
                style={{ textTransform: "none" }}
              >
                {"Upload photos which verify your job and income"}
              </Button>
            </label>
          </Grid>
          <Grid item xs={6}>
            <FormControlLabel
              control={<Checkbox value="consent" color="primary" />}
              label="I consent to the collection and use of my personal information."
            />
          </Grid>
        </Grid>
        <Button
          fullWidth
          variant="contained"
          color="primary"
          className={classes.submit}
        >
          Sign Up
        </Button>
      </form>
    </div>
  );
}
