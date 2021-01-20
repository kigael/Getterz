import React, { useCallback } from "react";
import axios from "axios";
import { Link, useHistory } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { sellerLogout, sellerLoading, sellerDone } from "../info/SellerInfo";
import useMediaQuery from "@material-ui/core/useMediaQuery";
import { useTheme } from "@material-ui/core/styles";
import { makeStyles } from "@material-ui/core/styles";
import RedeemIcon from "@material-ui/icons/Redeem";
import ArrowForwardIcon from "@material-ui/icons/ArrowForward";
import LocalAtmIcon from "@material-ui/icons/LocalAtm";
import BottomNavigation from "@material-ui/core/BottomNavigation";
import BottomNavigationAction from "@material-ui/core/BottomNavigationAction";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import Button from "@material-ui/core/Button";
import AccountBoxIcon from "@material-ui/icons/AccountBox";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";

const useStyles = makeStyles((theme) => ({
  footBar: {
    background: "#aed581",
    width: "100%",
    position: "fixed",
    bottom: 0,
  },
  logoutButton: {
    color: "white",
    backgroundColor: theme.palette.success.main,
    "&:hover": {
      backgroundColor: theme.palette.success.light,
    },
  },
}));

export default function SellerFootBar() {
  const classes = useStyles();
  const { header } = useSelector((state) => ({
    header: state.SellerInfo.header,
  }));
  const dispatch = useDispatch();
  const logout = () => dispatch(sellerLogout());
  const loading = () => dispatch(sellerLoading());
  const done = () => dispatch(sellerDone());
  const history = useHistory();
  const redirectTo = useCallback(() => history.push("/"), [history]);
  const sellerAPILogout = async (header) => {
    loading();
    await axios
      .post("/seller/logout", header)
      .then(function (response) {
        if (response.data.resultCode === "OK") {
          logout();
        } else if (response.data.description === "INVALID SESSION") {
          logout();
        }
      })
      .catch(function (error) {
        logout();
      });
    done();
    redirectTo();
  };
  const [openSellerLogout, setOpenSellerLogout] = React.useState(false);
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down("sm"));
  const handleClickOpenSellerLogout = () => {
    setOpenSellerLogout(true);
  };
  const handleCloseSellerLogout = () => {
    setOpenSellerLogout(false);
  };
  return (
    <div>
      <BottomNavigation showLabels className={classes.footBar}>
        <BottomNavigationAction
          component={Link}
          to="/seller/product"
          label="Product"
          icon={<RedeemIcon />}
        />
        <BottomNavigationAction
          component={Link}
          to="/seller/order"
          label="Order"
          icon={<ArrowForwardIcon />}
        />
        <BottomNavigationAction
          component={Link}
          to="/seller/withdraw"
          label="Withdraw"
          icon={<LocalAtmIcon />}
        />
        <BottomNavigationAction
          component={Link}
          to="/seller/manage"
          label="Manage"
          icon={<AccountBoxIcon />}
        />
        <BottomNavigationAction
          onClick={handleClickOpenSellerLogout}
          label="Logout"
          icon={<ExitToAppIcon />}
        />
      </BottomNavigation>
      <Dialog
        fullScreen={fullScreen}
        open={openSellerLogout}
        onClose={handleCloseSellerLogout}
      >
        <DialogTitle id="responsive-dialog-title">
          {"Logout Confirmation"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText>
            {header.data.name},{"Would you like to logout Getterz Seller?"}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            className={classes.logoutButton}
            autoFocus
            onClick={() => sellerAPILogout(header)}
          >
            {"YES"}
          </Button>
          <Button
            className={classes.logoutButton}
            onClick={handleCloseSellerLogout}
            autoFocus
          >
            {"NO"}
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
