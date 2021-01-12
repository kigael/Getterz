import React from "react";
import { useSelector } from "react-redux";
import {
  makeStyles,
  AppBar,
  Typography,
  Grid,
  Avatar,
} from "@material-ui/core";
import ShoppingBasketIcon from "@material-ui/icons/ShoppingBasket";
import StoreIcon from "@material-ui/icons/Store";
import InfoIcon from "@material-ui/icons/Info";
import SearchIcon from "@material-ui/icons/Search";
import ShopIcon from "@material-ui/icons/Shop";
import RateReviewIcon from "@material-ui/icons/RateReview";
import { Link } from "react-router-dom";

const useStyles = makeStyles((theme) => ({
  headBar: (props) => {
    return {
      background:
        props.Type === "Buyer"
          ? theme.palette.primary.main
          : props.Type === "Seller"
          ? theme.palette.success.main
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
  const { sellerLoading, sellerData, buyerLoading, buyerData } = useSelector(
    (state) => ({
      sellerLoading: state.SellerInfo.loading,
      sellerData: state.SellerInfo.header.data,
      buyerLoading: state.BuyerInfo.loading,
      buyerData: state.BuyerInfo.header.data,
    })
  );
  const classes = useStyles(props);
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
          <Grid item xs={12} sm={3} justify="center">
            <Link
              to={props.Type ? "/" + props.Type : "/"}
              style={{ color: "inherit", textDecoration: "inherit" }}
            >
              <Typography variant="h3" height="100%" className={classes.title}>
                [ Getterz{props.Type ? " " + props.Type : ""} ]
              </Typography>
            </Link>
          </Grid>
          {props.Type === "Seller" ? (
            <div>asd</div>
          ) : props.Type === "Buyer" ? (
            <>
              <Grid item xs={0} sm={5} />
              <Grid
                container
                item
                xs={12}
                sm={4}
                direction="row"
                justify="space-evenly"
              >
                <Grid
                  item
                  container
                  xs={1}
                  sm={1}
                  direction="column"
                  alignItems="flex-start"
                >
                  <Link
                    to="/buyer/search"
                    style={{ color: "inherit", textDecoration: "inherit" }}
                  >
                    <Typography variant="h5" className={classes.option}>
                      <SearchIcon />
                      Search
                    </Typography>
                  </Link>
                </Grid>
                <Grid
                  item
                  container
                  xs={1}
                  sm={1}
                  direction="column"
                  alignItems="flex-start"
                >
                  <Link
                    to="/buyer/purchase"
                    style={{ color: "inherit", textDecoration: "inherit" }}
                  >
                    <Typography variant="h5" className={classes.option}>
                      <ShopIcon />
                      Purchase
                    </Typography>
                  </Link>
                </Grid>
                <Grid
                  item
                  container
                  xs={1}
                  sm={1}
                  direction="column"
                  alignItems="flex-start"
                >
                  <Link
                    to="/buyer/review"
                    style={{ color: "inherit", textDecoration: "inherit" }}
                  >
                    <Typography variant="h5" className={classes.option}>
                      <RateReviewIcon />
                      Review
                    </Typography>
                  </Link>
                </Grid>
                {buyerData.loading ? (
                  <></>
                ) : (
                  <Grid
                    item
                    container
                    xs={1}
                    sm={1}
                    direction="column"
                    alignItems="flex-end"
                  >
                    <Link
                      to="/buyer/manage"
                      style={{ color: "inherit", textDecoration: "inherit" }}
                    >
                      <Avatar
                        alt={buyerData.name}
                        src="/static/images/profile_img/1.jpg"
                      />
                    </Link>
                    <Link
                      to="/buyer/manage"
                      style={{ color: "inherit", textDecoration: "inherit" }}
                    >
                      <Typography variant="caption" className={classes.account}>
                        {buyerData.emailAddress}
                      </Typography>
                    </Link>
                  </Grid>
                )}
              </Grid>
            </>
          ) : (
            <>
              <Grid item xs={0} sm={6} />
              <Grid
                container
                item
                xs={12}
                sm={3}
                direction="row"
                justify="space-evenly"
              >
                <Grid
                  item
                  container
                  xs={1}
                  sm={1}
                  direction="column"
                  alignItems="flex-start"
                >
                  <Link
                    to="/seller/login"
                    style={{ color: "inherit", textDecoration: "inherit" }}
                  >
                    <Typography variant="h5" className={classes.option}>
                      <StoreIcon />
                      Sell
                    </Typography>
                  </Link>
                </Grid>
                <Grid
                  item
                  container
                  xs={1}
                  sm={1}
                  direction="column"
                  alignItems="flex-start"
                >
                  <Link
                    to="/buyer/login"
                    style={{ color: "inherit", textDecoration: "inherit" }}
                  >
                    <Typography variant="h5" className={classes.option}>
                      <ShoppingBasketIcon />
                      Buy
                    </Typography>
                  </Link>
                </Grid>
                <Grid
                  item
                  container
                  xs={1}
                  sm={1}
                  direction="column"
                  alignItems="flex-start"
                >
                  <Link
                    to="/about"
                    style={{ color: "inherit", textDecoration: "inherit" }}
                  >
                    <Typography variant="h5" className={classes.option}>
                      <InfoIcon />
                      About
                    </Typography>
                  </Link>
                </Grid>
              </Grid>
            </>
          )}
        </Grid>
      </AppBar>
    </div>
  );
}
