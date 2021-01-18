import React, { useCallback } from "react";
import axios from "axios";
import { Link, useHistory } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { adminLogout, adminLoading, adminDone } from "../info/AdminInfo";
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
import DoneOutlineIcon from "@material-ui/icons/DoneOutline";
import DoneIcon from "@material-ui/icons/Done";
import AccountBalanceIcon from "@material-ui/icons/AccountBalance";
import MonetizationOnIcon from "@material-ui/icons/MonetizationOn";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";

const useStyles = makeStyles((theme) => ({
  footBar: {
    background: "#ffeb3b",
    width: "100%",
    position: "fixed",
    bottom: 0,
  },
}));

export default function AdminFootBar(props) {
  const classes = useStyles(props);
  const { adminHeader } = useSelector((state) => ({
    adminHeader: state.AdminInfo.header,
  }));
  const dispatch = useDispatch();
  const aLogout = () => dispatch(adminLogout());
  const aLoading = () => dispatch(adminLoading());
  const aDone = () => dispatch(adminDone());
  const history = useHistory();
  const aRedirecTo = useCallback(() => history.push("/"), [history]);
  const adminAPILogout = async (header) => {
    aLoading();
    await axios
      .post("/admin/logout", header)
      .then(function (response) {
        if (response.data.resultCode === "OK") {
          aLogout();
        } else if (response.data.description === "INVALID SESSION") {
          aLogout();
        }
        aDone();
      })
      .catch(function (error) {
        aLogout();
        aDone();
      });
    aRedirecTo();
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
  return (
    <div>
      <BottomNavigation showLabels className={classes.footBar}>
        <BottomNavigationAction
          component={Link}
          to="/admin/buyer_verify"
          label="Buyer verify"
          icon={<DoneIcon />}
        />
        <BottomNavigationAction
          component={Link}
          to="/admin/seller_verify"
          label="Seller verify"
          icon={<DoneOutlineIcon />}
        />
        <BottomNavigationAction
          component={Link}
          to="/admin/buyer_refund"
          label="Buyer refund"
          icon={<MonetizationOnIcon />}
        />
        <BottomNavigationAction
          component={Link}
          to="/admin/seller_withdraw"
          label="Seller withdraw"
          icon={<AccountBalanceIcon />}
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
            {adminHeader.data.name},{"Would you like to logout Getterz Buyer?"}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            autoFocus
            onClick={() => adminAPILogout(adminHeader)}
            color="primary"
          >
            {"YES"}
          </Button>
          <Button onClick={handleCloseBuyerLogout} color="warning" autoFocus>
            {"NO"}
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
