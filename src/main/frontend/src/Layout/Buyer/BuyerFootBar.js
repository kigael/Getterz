import React, { useCallback } from "react";
import axios from "axios";
import { Link, useHistory } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { buyerLogout, buyerLoading, buyerDone } from "../../info/BuyerInfo";
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
import SearchIcon from "@material-ui/icons/Search";
import ShopIcon from "@material-ui/icons/Shop";
import RateReviewIcon from "@material-ui/icons/RateReview";
import AccountBoxIcon from "@material-ui/icons/AccountBox";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";

const useStyles = makeStyles((theme) => ({
  footBar: {
    background: "#4fc3f7",
    width: "100%",
    position: "fixed",
    bottom: 0,
  },
}));

export default function BuyerFootBar() {
  const classes = useStyles();
  const { header } = useSelector((state) => ({
    header: state.BuyerInfo.header,
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
        } else if (response.data.resultCode === "ERROR") {
          bLogout();
        }
        bDone();
      })
      .catch(function (error) {
        bLogout();
        bDone();
      });
    bRedirecTo();
  };
  const [openBuyerLogout, setOpenBuyerLogout] = React.useState(false);
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down("sm"));
  const handleClickOpenLogout = () => {
    setOpenBuyerLogout(true);
  };
  const handleCloseLogout = () => {
    setOpenBuyerLogout(false);
  };
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
          onClick={handleClickOpenLogout}
          label="Logout"
          icon={<ExitToAppIcon />}
        />
      </BottomNavigation>
      <Dialog
        fullScreen={fullScreen}
        open={openBuyerLogout}
        onClose={handleCloseLogout}
      >
        <DialogTitle id="responsive-dialog-title">
          {"Logout Confirmation"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText>
            {header.data.name},{"Would you like to logout Getterz Buyer?"}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            autoFocus
            onClick={() => buyerAPILogout(header)}
            color="primary"
          >
            {"YES"}
          </Button>
          <Button onClick={handleCloseLogout} color="primary" autoFocus>
            {"NO"}
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
