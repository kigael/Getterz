import React from "react";
import { useSelector } from "react-redux";
import { makeStyles, Typography, Grid } from "@material-ui/core";
import Box from "@material-ui/core/Box";

const useStyles = makeStyles((theme) => ({
  box: {
    color: "#4fc3f7",
  },
  hopeYouGet: {
    textAlign: "center",
    color: theme.palette.primary.light,
  },
  whatYouWant: {
    textAlign: "center",
    color: theme.palette.primary.dark,
  },
  userName: {
    textAlign: "center",
    color: theme.palette.primary.main,
  },
}));

export default function GateBody() {
  const classes = useStyles();
  const header = useSelector((state) => state.BuyerInfo.header);
  return (
    <>
      {header.data.name ? (
        <Grid container item xs={12} sm={12} justify="center">
          <Grid item xs={0} sm={3} />
          <Grid item xs={12} sm={6}>
            <Box border={6} m="5rem" className={classes.box}>
              <Typography variant="h1" m="3rem" className={classes.hopeYouGet}>
                Hope you get
              </Typography>
              <br />
              <Typography variant="h2" m="3rem" className={classes.whatYouWant}>
                what you want
              </Typography>
              <br />
              <Typography variant="h1" m="3rem" className={classes.userName}>
                [ {header.data.name} ]
              </Typography>
            </Box>
          </Grid>
          <Grid item xs={0} sm={3} />
        </Grid>
      ) : (
        <></>
      )}
    </>
  );
}
