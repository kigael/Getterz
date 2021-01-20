import React from "react";
import { Link } from "react-router-dom";
import { makeStyles, Typography, Grid } from "@material-ui/core";
import Box from "@material-ui/core/Box";
import ShoppingBasketIcon from "@material-ui/icons/ShoppingBasket";
import StoreIcon from "@material-ui/icons/Store";

const useStyles = makeStyles((theme) => ({
  box: {
    color: "#9c27b0",
  },
  getterzWill: {
    textAlign: "center",
    color: theme.palette.success.light,
  },
  getYou: {
    textAlign: "center",
    color: theme.palette.primary.light,
  },
  anything: {
    textAlign: "center",
    color: theme.palette.secondary.light,
    textDecoration: "underline",
  },
  sellerIcon: {
    color: theme.palette.success.main,
    width: 50,
    height: 50,
  },
  sellerText: {
    color: theme.palette.success.main,
    fontSize: 50,
  },
  buyerIcon: {
    color: theme.palette.primary.main,
    width: 50,
    height: 50,
  },
  buyerText: {
    color: theme.palette.primary.main,
    fontSize: 50,
  },
}));

export default function GateBody() {
  const classes = useStyles();
  return (
    <>
      <Grid container item xs={12} justify="center">
        <Grid item xs={4} />
        <Grid item xs={4}>
          <Box border={6} m="5rem" className={classes.box}>
            <Typography variant="h2" m="3rem" className={classes.getterzWill}>
              Getterz will
            </Typography>
            <Typography variant="h3" m="3rem" className={classes.getYou}>
              get you
            </Typography>
            <Typography variant="h2" m="3rem" className={classes.anything}>
              anything
            </Typography>
          </Box>
        </Grid>
        <Grid item xs={4} />
      </Grid>
      <Grid container item xs={12} sm={12} direction="row" justify="center">
        <Grid
          container
          item
          xs={6}
          direction="column"
          alignItems="center"
          style={{ textAlign: "center" }}
        >
          <Link
            to="/seller/login"
            style={{ color: "inherit", textDecoration: "inherit" }}
          >
            <StoreIcon className={classes.sellerIcon} />
            <Typography className={classes.sellerText}>[ SELLER ]</Typography>
          </Link>
        </Grid>
        <Grid
          container
          item
          xs={6}
          direction="column"
          alignItems="center"
          style={{ textAlign: "center" }}
        >
          <Link
            to="/buyer/login"
            style={{ color: "inherit", textDecoration: "inherit" }}
          >
            <ShoppingBasketIcon className={classes.buyerIcon} />
            <Typography className={classes.buyerText}>[ BUYER ]</Typography>
          </Link>
        </Grid>
      </Grid>
    </>
  );
}
