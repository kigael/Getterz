import React from "react";
import { makeStyles, Typography, Grid, Box } from "@material-ui/core";

const useStyles = makeStyles((theme) => ({
  box: {
    color: "#9c27b0",
  },
  getterzTitle: {
    color: "white",
  },
  getterzText: {},
  sellerTitle: {
    color: theme.palette.success.main,
  },
  sellerText: {},
  buyerTitle: {
    color: theme.palette.primary.main,
  },
  buyerText: {},
  missionTitle: {
    color: theme.palette.secondary.main,
  },
  missionText: {},
}));

export default function AboutGetterzBody() {
  const classes = useStyles();
  return (
    <Grid container item xs={12} sm={12} direction="row" justify="center">
      <Grid item xs={0} sm={3} />
      <Grid container item xs={12} sm={6} direction="column">
        <Box border={6} className={classes.box}>
          <Typography variant="h2" gutterBottom>
            About{" "}
            <Typography
              variant="h2"
              gutterBottom
              className={classes.getterzTitle}
              display="inline"
            >
              Getterz
            </Typography>
          </Typography>
          <Typography variant="h5" gutterBottom>
            Getterz will always get you anything.
          </Typography>
          <Typography variant="h4" gutterBottom>
            What is{" "}
            <Typography
              variant="h4"
              gutterBottom
              className={classes.getterzTitle}
              display="inline"
            >
              Getterz?
            </Typography>
          </Typography>
          <Typography variant="subtitle1" gutterBottom display="inline">
            Getterz is...
          </Typography>
          <Typography variant="h4" gutterBottom>
            What is{" "}
            <Typography
              variant="h4"
              gutterBottom
              className={classes.sellerTitle}
              display="inline"
            >
              Seller?
            </Typography>
          </Typography>
          <Typography variant="body1" gutterBottom>
            Seller is...
          </Typography>
          <Typography variant="h4" gutterBottom>
            What is{" "}
            <Typography
              variant="h4"
              gutterBottom
              className={classes.buyerTitle}
              display="inline"
            >
              Buyer?
            </Typography>
          </Typography>
          <Typography variant="body1" gutterBottom>
            Buyer is...
          </Typography>
          <Typography variant="h4" gutterBottom>
            Our{" "}
            <Typography
              variant="h4"
              gutterBottom
              className={classes.missionTitle}
              display="inline"
            >
              Mission
            </Typography>
          </Typography>
          <Typography variant="body1" gutterBottom>
            Mission of Getterz is...
          </Typography>
        </Box>
      </Grid>
      <Grid item xs={0} sm={3} />
    </Grid>
  );
}
