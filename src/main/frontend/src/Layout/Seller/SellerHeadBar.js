import React from "react";
import { useSelector } from "react-redux";
import {
  makeStyles,
  AppBar,
  Typography,
  Grid,
  Avatar,
} from "@material-ui/core";
import RedeemIcon from "@material-ui/icons/Redeem";
import ArrowForwardIcon from "@material-ui/icons/ArrowForward";
import LocalAtmIcon from "@material-ui/icons/LocalAtm";
import { Link } from "react-router-dom";

const useStyles = makeStyles((theme) => ({
  headBar: {
    background: theme.palette.success.main,
    position: "static",
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

export default function SellerHeadBar() {
  const { loading, header } = useSelector((state) => ({
    loading: state.SellerInfo.loading,
    header: state.SellerInfo.header,
  }));
  const classes = useStyles();
  return (
    <div>
      <AppBar className={classes.headBar}>
        <Grid
          container
          item
          xs={12}
          sm={12}
          alignItems="center"
          justify="center"
        >
          <Grid item xs={3} justify="center">
            <Link
              to={"/seller"}
              style={{ color: "inherit", textDecoration: "inherit" }}
            >
              <Typography variant="h4" height="100%" className={classes.title}>
                [ Getterz Seller ]
              </Typography>
            </Link>
          </Grid>
          <Grid item xs={5} />
          <Grid container item xs={4} direction="row" justify="space-evenly">
            <Grid
              item
              container
              xs={1}
              direction="column"
              alignItems="flex-start"
            >
              <Link
                to="/seller/product"
                style={{ color: "inherit", textDecoration: "inherit" }}
              >
                <Typography variant="subtitle1" className={classes.option}>
                  <RedeemIcon />
                  Product
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
                to="/seller/order"
                style={{ color: "inherit", textDecoration: "inherit" }}
              >
                <Typography variant="subtitle1" className={classes.option}>
                  <ArrowForwardIcon />
                  Order
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
                to="/seller/withdraw"
                style={{ color: "inherit", textDecoration: "inherit" }}
              >
                <Typography variant="subtitle1" className={classes.option}>
                  <LocalAtmIcon />
                  Withdraw
                </Typography>
              </Link>
            </Grid>
            {loading ? (
              <></>
            ) : (
              <Grid
                item
                container
                xs={1}
                direction="column"
                alignItems="flex-end"
              >
                <Link
                  to="/seller/manage"
                  style={{ color: "inherit", textDecoration: "inherit" }}
                >
                  <Avatar
                    alt={header.data.name}
                    src={
                      "/upload/seller/profile_image/" +
                      header.data.profileImageName
                    }
                  />
                </Link>
                <Link
                  to="/seller/manage"
                  style={{ color: "inherit", textDecoration: "inherit" }}
                >
                  <Typography variant="caption" className={classes.account}>
                    {header.data.emailAddress}
                  </Typography>
                </Link>
              </Grid>
            )}
          </Grid>
        </Grid>
      </AppBar>
    </div>
  );
}
