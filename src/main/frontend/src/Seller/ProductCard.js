import React, { useState } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { sellerLoading, sellerDone } from "../info/SellerInfo";
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

export default function ProductCard({ classes, product, session, refresh }) {
  const baseDir = "/upload/product/profile_image/";
  const [openProductDelete, setOpenProductDelete] = useState(false);
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down("sm"));
  const handleClickOpenProductDelete = () => {
    setOpenProductDelete(true);
  };
  const handleCloseProductDelete = () => {
    setOpenProductDelete(false);
  };
  const dispatch = useDispatch();
  const loading = () => dispatch(sellerLoading());
  const done = () => dispatch(sellerDone());
  const DeleteProduct = async () => {
    loading();
    await axios
      .delete("/seller/product/" + product.id, {
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
    handleCloseProductDelete();
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
          <Button size="small" href={"/#/seller/product/" + product.id}>
            View
          </Button>
          <Button size="small">Edit</Button>
          <Button size="small" onClick={handleClickOpenProductDelete}>
            Delete
          </Button>
        </CardActions>
      </Card>
      <Dialog
        fullScreen={fullScreen}
        open={openProductDelete}
        onClose={handleCloseProductDelete}
      >
        <DialogTitle id="responsive-dialog-title">
          {"Product delete Confirmation"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText>
            {"Would you like to delete " + product.name + " ?"}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            className={classes.logoutButton}
            autoFocus
            onClick={DeleteProduct}
          >
            {"YES"}
          </Button>
          <Button
            className={classes.logoutButton}
            onClick={handleCloseProductDelete}
            autoFocus
          >
            {"NO"}
          </Button>
        </DialogActions>
      </Dialog>
    </Grid>
  );
}
