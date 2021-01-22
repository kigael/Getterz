import React from "react";
import { Link } from "react-router-dom";
import { makeStyles } from "@material-ui/core/styles";
import BottomNavigation from "@material-ui/core/BottomNavigation";
import BottomNavigationAction from "@material-ui/core/BottomNavigationAction";
import ShoppingBasketIcon from "@material-ui/icons/ShoppingBasket";
import StoreIcon from "@material-ui/icons/Store";
import InfoIcon from "@material-ui/icons/Info";

const useStyles = makeStyles((theme) => ({
  footBar: {
    background: "#ed4b82",
    width: "100%",
    position: "fixed",
    bottom: 0,
  },
}));

export default function DefaultFootBar() {
  const classes = useStyles();
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
