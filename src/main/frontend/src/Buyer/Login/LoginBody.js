import React, { useState, useCallback } from "react";
import { useHistory } from "react-router-dom";
import { useDispatch } from "react-redux";
import { buyerLogin, buyerLoading, buyerDone } from "../../info/BuyerInfo";
import axios from "axios";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Link from "@material-ui/core/Link";
import Grid from "@material-ui/core/Grid";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";

const useStyles = makeStyles((theme) => ({
  paper: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.primary.main,
  },
  form: {
    width: "100%",
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
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
  const login = (header) => dispatch(buyerLogin(header));
  const loading = () => dispatch(buyerLoading());
  const done = () => dispatch(buyerDone());
  const history = useHistory();
  const redirecTo = useCallback(() => history.push("/buyer"), [history]);
  const Login = async (inputs, setInputs) => {
    const setError = (e) => {
      setInputs({
        ...inputs,
        error: e,
      });
    };
    loading();
    await axios
      .post("/buyer/login", {
        transactionTime: "",
        transactionType: "BUYER LOGIN",
        data: {
          emailAddress: inputs.email,
          password: inputs.password,
        },
      })
      .then(function (response) {
        if (response.data.resultCode === "OK") {
          login(response.data);
          done();
          redirecTo();
        } else if (response.data.resultCode === "ERROR") {
          setError(response.data.description);
          done();
        }
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
          {"BUYER LOGIN"}
        </Typography>
        <form className={classes.form} noValidate>
          <TextField
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
          {error === "BUYER NOT FOUND" ? (
            <Typography className={classes.error} variant="body1">
              ! BUYER NOT FOUND !
            </Typography>
          ) : error === "PASSWORD MISMATCH" ? (
            <Typography className={classes.error} variant="body1">
              ! PASSWORD MISMATCH !
            </Typography>
          ) : (
            <></>
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
          <Grid container>
            <Grid item xs>
              <Link href="/#/buyer/forgot" variant="body2">
                {"Forgot password?"}
              </Link>
            </Grid>
            <Grid item>
              <Link href="/#/buyer/signup" variant="body2">
                {"Not a buyer yet? Sign Up!"}
              </Link>
            </Grid>
          </Grid>
        </form>
      </Grid>
      <Grid item xs={0} sm={4} />
    </Grid>
  );
}
