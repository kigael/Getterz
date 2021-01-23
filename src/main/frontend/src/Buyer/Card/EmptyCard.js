import React from "react";
import {
  Grid,
  Typography,
  Card,
  CardMedia,
  CardContent,
  CardActions,
  Button,
} from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";

const useStyles = makeStyles((theme) => ({
  card: {
    height: "100%",
    display: "flex",
    flexDirection: "column",
  },
  cardMedia: {
    paddingTop: "56.25%", // 16:9
  },
  cardContent: {
    flexGrow: 1,
  },
}));

export default function EmptyCard() {
  const classes = useStyles();
  return (
    <Grid item xs={3}>
      <Card className={classes.card}>
        <CardMedia className={classes.cardMedia} />
        <CardContent className={classes.cardContent}>
          <Typography
            gutterBottom
            variant="h5"
            component="h2"
            style={{ color: "white", backgroundColor: "white" }}
          >
            EMPTY
          </Typography>
          <Typography style={{ color: "white", backgroundColor: "white" }}>
            EMPTY
          </Typography>
        </CardContent>
        <CardActions>
          <Button
            style={{ color: "white", backgroundColor: "white" }}
            size="small"
            disabled
          >
            EMPTY
          </Button>
          <Button
            style={{ color: "white", backgroundColor: "white" }}
            size="small"
            disabled
          >
            EMPTY
          </Button>
        </CardActions>
      </Card>
    </Grid>
  );
}
