import React from "react";
import { makeStyles, Paper, Grid } from "@material-ui/core";
import AdminGateWallpaper from "../img/admin-gate-wallpaper.jpg";

const useStyles = makeStyles((theme) => ({
  wallpaper: {
    backgroundImage: `url(${AdminGateWallpaper})`,
    width: "100%",
    height: "100%",
    style: { padding: "0px", height: "100vh" },
  },
}));

export default function AdminBodyPaper({ Body }) {
  const classes = useStyles();
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
        {Body}
      </Grid>
    </Paper>
  );
}
