import React, { useState, useCallback } from "react";
import { useHistory } from "react-router-dom";
import { useDispatch } from "react-redux";
import { sellerLogin, sellerLoading, sellerDone } from "../info/SellerInfo";
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
    backgroundColor: theme.palette.success.main,
  },
  form: {
    width: "100%",
    marginTop: theme.spacing(1),
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
  submit: {
    margin: theme.spacing(3, 0, 2),
    color: "white",
    backgroundColor: theme.palette.success.main,
    "&:hover": {
      backgroundColor: theme.palette.success.light,
    },
  },
  error: {
    textAlign: "center",
    color: "#ff5722",
  },
  link: {
    color: theme.palette.success.main,
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
  const login = (header) => dispatch(sellerLogin(header));
  const loading = () => dispatch(sellerLoading());
  const done = () => dispatch(sellerDone());
  const history = useHistory();
  const redirecTo = useCallback(() => history.push("/seller"), [history]);
  const Login = async (inputs, setInputs) => {
    const setError = (e) => {
      setInputs({
        ...inputs,
        error: e,
      });
    };
    loading();
    await axios
      .post("/seller/login", {
        transactionTime: "",
        transactionType: "SELLER LOGIN",
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
          {"SELLER LOGIN"}
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
          {error === "SELLER NOT FOUND" ? (
            <Typography className={classes.error} variant="body1">
              ! SELLER NOT FOUND !
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
              <Link
                href="/#/seller/forgot"
                variant="body2"
                className={classes.link}
              >
                {"Forgot password?"}
              </Link>
            </Grid>
            <Grid item>
              <Link
                href="/#/seller/signup"
                variant="body2"
                className={classes.link}
              >
                {"Not a seller yet? Sign Up!"}
              </Link>
            </Grid>
          </Grid>
        </form>
      </Grid>
      <Grid item xs={0} sm={4} />
    </Grid>
  );
}
