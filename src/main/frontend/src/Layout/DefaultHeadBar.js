import React from "react";
import { makeStyles, AppBar, Typography, Grid } from "@material-ui/core";
import ShoppingBasketIcon from "@material-ui/icons/ShoppingBasket";
import StoreIcon from "@material-ui/icons/Store";
import InfoIcon from "@material-ui/icons/Info";
import { Link } from "react-router-dom";

const useStyles = makeStyles((theme) => ({
  headBar: (props) => {
    return {
      background:
        props.Type === "Buyer"
          ? theme.palette.primary.main
          : props.Type === "Seller"
          ? theme.palette.success.main
          : props.Type === "Admin"
          ? theme.palette.warning.main
          : theme.palette.secondary.main,
      position: "static",
    };
  },
  title: {
    textAlign: "center",
    "&:hover": {
      textDecoration: "underline",
    },
  },
  option: {
    textAlign: "center",
    "&:hover": {
      textDecoration: "underline",
    },
    profileName: {
      textAlign: "center",
    },
  },
}));

export default function HeadBar(props) {
  const classes = useStyles();
  return (
    <div>
      <AppBar className={classes.headBar}>
        <Grid container item xs={12} alignItems="center" justify="center">
          <Grid item xs={3} justify="center">
            <Link
              to={"/"}
              style={{ color: "inherit", textDecoration: "inherit" }}
            >
              <Typography variant="h4" height="100%" className={classes.title}>
                [ Getterz ]
              </Typography>
            </Link>
          </Grid>
          <Grid item xs={6} />
          <Grid container item xs={3} direction="row" justify="space-evenly">
            <Grid
              item
              container
              xs={1}
              direction="column"
              alignItems="flex-start"
            >
              <Link
                to="/seller/login"
                style={{ color: "inherit", textDecoration: "inherit" }}
              >
                <Typography variant="subtitle1" className={classes.option}>
                  <StoreIcon />
                  Sell
                </Typography>
              </Link>
            </Grid>
            <Grid
              item
              container
              xs={1}
              direction="column"
              alignItems="flex-start"
            >
              <Link
                to="/buyer/login"
                style={{ color: "inherit", textDecoration: "inherit" }}
              >
                <Typography variant="subtitle1" className={classes.option}>
                  <ShoppingBasketIcon />
                  Buy
                </Typography>
              </Link>
            </Grid>
            <Grid
              item
              container
              xs={1}
              direction="column"
              alignItems="flex-start"
            >
              <Link
                to="/about"
                style={{ color: "inherit", textDecoration: "inherit" }}
              >
                <Typography variant="subtitle1" className={classes.option}>
                  <InfoIcon />
                  About
                </Typography>
              </Link>
            </Grid>
          </Grid>
        </Grid>
      </AppBar>
    </div>
  );
}
