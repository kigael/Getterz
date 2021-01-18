import React from "react";
import { useSelector } from "react-redux";
import {
  makeStyles,
  AppBar,
  Typography,
  Grid,
  Avatar,
} from "@material-ui/core";
import SearchIcon from "@material-ui/icons/Search";
import ShopIcon from "@material-ui/icons/Shop";
import RateReviewIcon from "@material-ui/icons/RateReview";
import { Link } from "react-router-dom";

const useStyles = makeStyles((theme) => ({
  headBar: {
    background: theme.palette.primary.main,
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

export default function BuyerHeadBar() {
  const { loading, header } = useSelector((state) => ({
    loading: state.BuyerInfo.loading,
    header: state.BuyerInfo.header,
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
              to={"/buyer"}
              style={{ color: "inherit", textDecoration: "inherit" }}
            >
              <Typography variant="h4" height="100%" className={classes.title}>
                [ Getterz Buyer ]
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
                to="/buyer/search"
                style={{ color: "inherit", textDecoration: "inherit" }}
              >
                <Typography variant="subtitle1" className={classes.option}>
                  <SearchIcon />
                  Search
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
                to="/buyer/purchase"
                style={{ color: "inherit", textDecoration: "inherit" }}
              >
                <Typography variant="subtitle1" className={classes.option}>
                  <ShopIcon />
                  Purchase
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
                to="/buyer/review"
                style={{ color: "inherit", textDecoration: "inherit" }}
              >
                <Typography variant="subtitle1" className={classes.option}>
                  <RateReviewIcon />
                  Review
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
                  to="/buyer/manage"
                  style={{ color: "inherit", textDecoration: "inherit" }}
                >
                  <Avatar
                    alt={header.data.name}
                    src={
                      "/upload/buyer/profile_image/" +
                      header.data.profileImageName
                    }
                  />
                </Link>
                <Link
                  to="/buyer/manage"
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
