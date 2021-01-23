import React, { useState } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { buyerLoading, buyerDone } from "../../info/BuyerInfo";
import useMediaQuery from "@material-ui/core/useMediaQuery";
import { useTheme } from "@material-ui/core/styles";
import {
  Grid,
  Typography,
  Card,
  CardMedia,
  CardContent,
  CardActions,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
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

export default function ProductCard({ product, session, refresh }) {
  const classes = useStyles();
  const baseDir = "/upload/product/profile_image/";
  const [openProductPurchase, setOpenProductPurchase] = useState(false);
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down("sm"));
  const handleClickOpenProductPurchase = () => {
    setOpenProductPurchase(true);
  };
  const handleCloseProductPurchase = () => {
    setOpenProductPurchase(false);
  };
  const dispatch = useDispatch();
  const loading = () => dispatch(buyerLoading());
  const done = () => dispatch(buyerDone());
  const PurchaseProduct = async () => {
    loading();
    await axios
      .post("/buyer/product/purchase/" + product.id, {
        params: {
          getterz_session: session,
        },
      })
      .then(function (response) {
        done();
      })
      .catch(function (error) {
        done();
      });
    handleCloseProductPurchase();
    refresh();
  };
  return (
    <Grid item key={product} xs={3}>
      <Card className={classes.card}>
        <CardMedia
          className={classes.cardMedia}
          image={baseDir + product.profileImageName}
          title={product.name}
        />
        <CardContent className={classes.cardContent}>
          <Typography gutterBottom variant="h5" component="h2">
            {product.name}
          </Typography>
          <Typography>${product.cost}</Typography>
        </CardContent>
        <CardActions>
          <Button size="small" href={"/#/buyer/product/" + product.id}>
            View
          </Button>
          <Button size="small" onClick={handleClickOpenProductPurchase}>
            Purchase
          </Button>
        </CardActions>
      </Card>
      <Dialog
        fullScreen={fullScreen}
        open={openProductPurchase}
        onClose={handleCloseProductPurchase}
      >
        <DialogTitle id="responsive-dialog-title">
          {"Product delete Confirmation"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText>
            {"Would you like to purchase " + product.name + " ?"}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            className={classes.logoutButton}
            autoFocus
            onClick={PurchaseProduct}
          >
            {"YES"}
          </Button>
          <Button
            className={classes.logoutButton}
            onClick={handleCloseProductPurchase}
            autoFocus
          >
            {"NO"}
          </Button>
        </DialogActions>
      </Dialog>
    </Grid>
  );
}
