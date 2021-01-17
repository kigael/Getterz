import React from "react";
import { useSelector } from "react-redux";
import { makeStyles, Typography, Grid } from "@material-ui/core";
import Box from "@material-ui/core/Box";

const useStyles = makeStyles((theme) => ({
  box: {
    color: "#ffeb3b",
  },
  hopeYouGet: {
    textAlign: "center",
    color: theme.palette.warning.light,
  },
  whatYouWant: {
    textAlign: "center",
    color: theme.palette.warning.dark,
  },
  userName: {
    textAlign: "center",
    color: theme.palette.warning.main,
  },
}));

export default function GateBody() {
  const classes = useStyles();
  const header = useSelector((state) => state.AdminInfo.header);
  return (
    <>
      {header ? (
        <Grid container item xs={12} sm={12} justify="center">
          <Grid item xs={0} sm={4} />
          <Grid item xs={12} sm={4}>
            <Box border={6} m="5rem" className={classes.box}>
              <Typography variant="h1" m="3rem" className={classes.hopeYouGet}>
                Dare not
              </Typography>
              <br />
              <Typography variant="h2" m="3rem" className={classes.whatYouWant}>
                touch the
              </Typography>
              <br />
              <Typography variant="h1" m="3rem" className={classes.userName}>
                Sun
              </Typography>
            </Box>
          </Grid>
          <Grid item xs={0} sm={4} />
        </Grid>
      ) : (
        <></>
      )}
    </>
  );
}
