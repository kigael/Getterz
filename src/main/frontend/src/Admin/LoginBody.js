import React, { useState, useCallback } from "react";
import { useHistory } from "react-router-dom";
import { useDispatch } from "react-redux";
import { adminLogin, adminLoading, adminDone } from "../info/AdminInfo";
import axios from "axios";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Grid from "@material-ui/core/Grid";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";

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

export default function LoginBody() {
  const classes = useStyles();
  const [inputs, setInputs] = useState({
    email: "",
    password: "",
    error: "",
  });
  const { email, password, error } = inputs;
  const onChange = (e) => {
    setInputs({
      ...inputs,
      [e.target.name]: e.target.value,
    });
  };
  const dispatch = useDispatch();
  const login = (header) => dispatch(adminLogin(header));
  const loading = () => dispatch(adminLoading());
  const done = () => dispatch(adminDone());
  const history = useHistory();
  const redirecTo = useCallback(() => history.push("/admin"), [history]);
  const Login = async (inputs, setInputs) => {
    const setError = (e) => {
      setInputs({
        ...inputs,
        error: e,
      });
    };
    loading();
    await axios
      .post("/admin/login", {
        transactionTime: "",
        transactionType: "ADMIN LOGIN",
        data: {
          emailAddress: inputs.email,
          password: inputs.password,
        },
      })
      .then(function (response) {
        if (response.data.resultCode === "OK") {
          login(response.data);
          redirecTo();
        } else if (response.data.resultCode === "ERROR") {
          setError(response.data.description);
        }
        done();
      })
      .catch(function (error) {
        done();
      });
  };
  return (
    <Grid container xs={12} sm={12} direction="row" justify="center">
      <Grid item xs={0} sm={4} />
      <Grid
        container
        item
        xs={12}
        sm={4}
        direction="column"
        alignItems="center"
      >
        <Avatar className={classes.avatar}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          {"ADMIN LOGIN"}
        </Typography>
        <form className={classes.form} noValidate>
          <TextField
            className={classes.inputField}
            value={email}
            onChange={onChange}
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="email"
            label="Email Address"
            name="email"
            autoComplete="email"
            autoFocus
          />
          <TextField
            className={classes.inputField}
            value={password}
            onChange={onChange}
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password"
            autoComplete="current-password"
          />
          {error === "" ? (
            <></>
          ) : (
            <Typography className={classes.error} variant="body1">
              {error}
            </Typography>
          )}
          <Button
            fullWidth
            variant="contained"
            color="primary"
            onClick={() => Login(inputs, setInputs)}
            className={classes.submit}
          >
            Login
          </Button>
        </form>
      </Grid>
      <Grid item xs={0} sm={4} />
    </Grid>
  );
}
