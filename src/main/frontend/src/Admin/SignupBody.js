import React, { useState } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { adminLoading, adminDone } from "../info/AdminInfo";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Grid from "@material-ui/core/Grid";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import MuiPhoneNumber from "material-ui-phone-number";

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.warning.main,
  },
  form: {
    width: "50%", // Fix IE 11 issue.
    marginTop: theme.spacing(3),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
    color: "white",
    backgroundColor: theme.palette.warning.main,
    "&:hover": {
      backgroundColor: theme.palette.warning.light,
    },
  },
  inputField: {
    "& label.Mui-focused": {
      color: "white",
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: theme.palette.warning.dark,
    },
    "& .MuiOutlinedInput-root": {
      "& fieldset": {
        borderColor: "white",
      },
      "&:hover fieldset": {
        borderColor: "white",
      },
      "&.Mui-focused fieldset": {
        borderColor: theme.palette.warning.dark,
      },
    },
  },
  error: {
    textAlign: "center",
    color: "#ff5722",
  },
}));

export default function SignupBody() {
  const classes = useStyles();
  const [inputs, setInputs] = useState({
    emailAddress: "",
    password: "",
    passwordCheck: "",
    cellNumber: "",
    key: "",
    error: "",
  });
  const {
    emailAddress,
    password,
    passwordCheck,
    cellNumber,
    key,
    error,
  } = inputs;
  const onChange = (e) => {
    setInputs({
      ...inputs,
      [e.target.name]: e.target.value,
    });
  };
  const handlePhoneChange = (value) => {
    if (value) {
      setInputs({
        ...inputs,
        cellNumber: value,
      });
    }
  };
  const dispatch = useDispatch();
  const loading = () => dispatch(adminLoading());
  const done = () => dispatch(adminDone());
  const Signup = async (inputs, setInputs) => {
    const setError = (e) => {
      setInputs({
        ...inputs,
        error: e,
      });
    };
    if (inputs.password !== inputs.passwordCheck) {
      setError("PASSWORD MISMATCH");
    } else {
      loading();
      await axios
        .post("/admin/signup", {
          transactionTime: "",
          transactionType: "ADMIN SIGNUP",
          data: {
            password: inputs.password,
            emailAddress: inputs.emailAddress,
            cellNumber: inputs.cellNumber,
            key: inputs.key,
          },
        })
        .then(function (response) {
          if (response.data.resultCode === "OK") {
            setError("ADMIN CREATE SUCCESS");
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
          <Grid item xs={12}>
            <TextField
              className={classes.inputField}
              variant="outlined"
              required
              fullWidth
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
              value={password}
              onChange={onChange}
              name="password"
              label="Password"
              type="password"
              id="password"
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              className={classes.inputField}
              variant="outlined"
              required
              fullWidth
              value={passwordCheck}
              onChange={onChange}
              name="passwordCheck"
              label="Check Password"
              type="password"
              id="passwordCheck"
            />
          </Grid>
          <Grid item xs={6}>
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
          <Grid item xs={6}>
            <TextField
              className={classes.inputField}
              variant="outlined"
              required
              fullWidth
              value={key}
              onChange={onChange}
              name="key"
              label="Key"
              type="password"
              id="adminCreateKey"
            />
          </Grid>
        </Grid>
        {error === "" ? (
          <div></div>
        ) : (
          <Typography className={classes.error}>{error}</Typography>
        )}
        <Button
          fullWidth
          variant="contained"
          className={classes.submit}
          onClick={() => Signup(inputs, setInputs)}
        >
          Sign Up
        </Button>
      </form>
    </div>
  );
}
