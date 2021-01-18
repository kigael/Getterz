import React, { useCallback } from "react";
import axios from "axios";
import { Link, useHistory } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { buyerLogout, buyerLoading, buyerDone } from "../info/BuyerInfo";
import useMediaQuery from "@material-ui/core/useMediaQuery";
import { useTheme } from "@material-ui/core/styles";
import { makeStyles } from "@material-ui/core/styles";
import BottomNavigation from "@material-ui/core/BottomNavigation";
import BottomNavigationAction from "@material-ui/core/BottomNavigationAction";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import Button from "@material-ui/core/Button";
import ShoppingBasketIcon from "@material-ui/icons/ShoppingBasket";
import SearchIcon from "@material-ui/icons/Search";
import ShopIcon from "@material-ui/icons/Shop";
import RateReviewIcon from "@material-ui/icons/RateReview";
import StoreIcon from "@material-ui/icons/Store";
import InfoIcon from "@material-ui/icons/Info";
import AccountBoxIcon from "@material-ui/icons/AccountBox";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";

const useStyles = makeStyles((theme) => ({
  footBar: (props) => ({
    background:
      props.Type === "Buyer"
        ? "#4fc3f7"
        : props.Type === "Seller"
        ? "#aed581"
        : props.Type === "Admin"
        ? "#ffeb3b"
        : "#ed4b82",
    width: "100%",
    position: "fixed",
    bottom: 0,
  }),
}));

export default function SellerFootBar(props) {
  const classes = useStyles(props);
  const { sellerHeader, buyerHeader } = useSelector((state) => ({
    sellerHeader: state.SellerInfo.header,
    buyerHeader: state.BuyerInfo.header,
  }));
  const dispatch = useDispatch();
  const bLogout = () => dispatch(buyerLogout());
  const bLoading = () => dispatch(buyerLoading());
  const bDone = () => dispatch(buyerDone());
  const history = useHistory();
  const bRedirecTo = useCallback(() => history.push("/"), [history]);
  const buyerAPILogout = async (header) => {
    bLoading();
    await axios
      .post("/buyer/logout", header)
      .then(function (response) {
        if (response.data.resultCode === "OK") {
          bLogout();
        } else if (response.data.description === "INVALID SESSION") {
          //show that session is invalid
          bLogout();
        }
      })
      .catch(function (error) {
        console.log(error);
      });
    bDone();
    bRedirecTo();
  };
  const [openBuyerLogout, setOpenBuyerLogout] = React.useState(false);
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down("sm"));
  const handleClickOpenBuyerLogout = () => {
    setOpenBuyerLogout(true);
  };
  const handleCloseBuyerLogout = () => {
    setOpenBuyerLogout(false);
  };
  switch (props.Type) {
    case "Admin":
      return (
        <div>
          <BottomNavigation showLabels className={classes.footBar}>
            {"ADMIN"}
          </BottomNavigation>
        </div>
      );
    case "Seller":
      return <div></div>;
    case "Buyer":
      return (
        <div>
          <BottomNavigation showLabels className={classes.footBar}>
            <BottomNavigationAction
              component={Link}
              to="/buyer/search"
              label="Search"
              icon={<SearchIcon />}
            />
            <BottomNavigationAction
              component={Link}
              to="/buyer/purchase"
              label="Purchase"
              icon={<ShopIcon />}
            />
            <BottomNavigationAction
              component={Link}
              to="/buyer/review"
              label="Review"
              icon={<RateReviewIcon />}
            />
            <BottomNavigationAction
              component={Link}
              to="/buyer/manage"
              label="Manage"
              icon={<AccountBoxIcon />}
            />
            <BottomNavigationAction
              onClick={handleClickOpenBuyerLogout}
              label="Logout"
              icon={<ExitToAppIcon />}
            />
          </BottomNavigation>
          <Dialog
            fullScreen={fullScreen}
            open={openBuyerLogout}
            onClose={handleCloseBuyerLogout}
          >
            <DialogTitle id="responsive-dialog-title">
              {"Logout Confirmation"}
            </DialogTitle>
            <DialogContent>
              <DialogContentText>
                {buyerHeader.data.name},
                {"Would you like to logout Getterz Buyer?"}
              </DialogContentText>
            </DialogContent>
            <DialogActions>
              <Button
                autoFocus
                onClick={() => buyerAPILogout(buyerHeader)}
                color="primary"
              >
                {"YES"}
              </Button>
              <Button
                onClick={handleCloseBuyerLogout}
                color="primary"
                autoFocus
              >
                {"NO"}
              </Button>
            </DialogActions>
          </Dialog>
        </div>
      );
    default:
      return (
        <div>
          <BottomNavigation showLabels className={classes.footBar}>
            <BottomNavigationAction
              component={Link}
              to="/seller/login"
              label="Sell"
              icon={<StoreIcon />}
            />
            <BottomNavigationAction
              component={Link}
              to="/buyer/login"
              label="Buy"
              icon={<ShoppingBasketIcon />}
            />
            <BottomNavigationAction
              component={Link}
              to="/about"
              label="About"
              icon={<InfoIcon />}
            />
          </BottomNavigation>
        </div>
      );
  }
}
