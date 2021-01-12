import React from "react";
import { makeStyles, Paper, Grid } from "@material-ui/core";
import GateWallpaper from "../img/gate-wallpaper.png";
import BuyerGateWallpaper from "../img/buyer-gate-wallpaper.jpg";

const useStyles = makeStyles((theme) => ({
  wallpaper: (props) => ({
    backgroundImage:
      props.Type === "Buyer"
        ? `url(${BuyerGateWallpaper})`
        : `url(${GateWallpaper})`,
    width: "100%",
    height: "100%",
    style: { padding: "0px", height: "100vh" },
  }),
}));

export default function BodyPaper(props) {
  const classes = useStyles(props);
  return (
    <Paper variant="elevation" className={classes.wallpaper}>
      <Grid
        container
        spacing={0}
        direction="column"
        alignItems="center"
        justify="center"
        style={{ minHeight: "100vh" }}
      >
        {props.InsideTag}
      </Grid>
    </Paper>
  );
}
