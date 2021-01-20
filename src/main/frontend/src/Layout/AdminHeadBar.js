import React from "react";
import { useSelector } from "react-redux";
import {
  makeStyles,
  AppBar,
  Typography,
  Grid,
  Avatar,
} from "@material-ui/core";
import DoneOutlineIcon from "@material-ui/icons/DoneOutline";
import DoneIcon from "@material-ui/icons/Done";
import AccountBalanceIcon from "@material-ui/icons/AccountBalance";
import MonetizationOnIcon from "@material-ui/icons/MonetizationOn";
import PersonIcon from "@material-ui/icons/Person";
import { Link } from "react-router-dom";

const useStyles = makeStyles((theme) => ({
  headBar: {
    background: theme.palette.warning.main,
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

export default function AdminHeadBar() {
  const { adminLoading, admin } = useSelector((state) => ({
    adminLoading: state.AdminInfo.loading,
    admin: state.AdminInfo.header.data,
  }));
  const classes = useStyles();
  return (
    <div>
      <AppBar className={classes.headBar}>
        <Grid container item xs={12} alignItems="center" justify="center">
          <Grid item xs={3} justify="center">
            <Link
              to={"/admin"}
              style={{ color: "inherit", textDecoration: "inherit" }}
            >
              <Typography variant="h4" height="100%" className={classes.title}>
                [ Getterz Admin ]
              </Typography>
            </Link>
          </Grid>
          <Grid item xs={4} />
          <Grid container item xs={5} direction="row" justify="space-evenly">
            <Grid
              item
              container
              xs={1}
              direction="column"
              alignItems="flex-start"
            >
              <Link
                to="/admin/buyer_verify"
                style={{ color: "inherit", textDecoration: "inherit" }}
              >
                <Typography variant="subtitle1" className={classes.option}>
                  <DoneIcon />
                  Buyer verify
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
                to="/admin/seller_verify"
                style={{ color: "inherit", textDecoration: "inherit" }}
              >
                <Typography variant="subtitle1" className={classes.option}>
                  <DoneOutlineIcon />
                  Seller verify
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
                to="/admin/buyer_refund"
                style={{ color: "inherit", textDecoration: "inherit" }}
              >
                <Typography variant="subtitle1" className={classes.option}>
                  <MonetizationOnIcon />
                  Buyer refund
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
                to="/admin/seller_withdraw"
                style={{ color: "inherit", textDecoration: "inherit" }}
              >
                <Typography variant="subtitle1" className={classes.option}>
                  <AccountBalanceIcon />
                  Seller withdraw
                </Typography>
              </Link>
            </Grid>
            {adminLoading ? (
              <></>
            ) : (
              <Grid
                item
                container
                xs={1}
                direction="column"
                alignItems="flex-end"
              >
                <Avatar>
                  <PersonIcon />
                </Avatar>
                <Typography variant="caption" className={classes.account}>
                  {admin.emailAddress}
                </Typography>
              </Grid>
            )}
          </Grid>
        </Grid>
      </AppBar>
    </div>
  );
}
