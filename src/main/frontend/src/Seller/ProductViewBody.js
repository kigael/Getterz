import React, { useState, useCallback } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { useHistory } from "react-router-dom";
import { sellerLoading, sellerDone } from "../info/SellerInfo";
import {
  Grid,
  Card,
  CardMedia,
  Typography,
  CardContent,
  Chip,
  Button,
  LinearProgress,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
} from "@material-ui/core";
import IframeResizer from "iframe-resizer-react";
import { makeStyles, withStyles } from "@material-ui/core/styles";
import useMediaQuery from "@material-ui/core/useMediaQuery";
import { useTheme } from "@material-ui/core/styles";

function LinearProgressWithLabel(props) {
  const StyledLinearProgress = withStyles({
    colorPrimary: {
      backgroundColor: props.colorPrimary,
    },
    barColorPrimary: {
      backgroundColor: props.barColorPrimary,
    },
  })(LinearProgress);
  return (
    <Grid container item direction="row" justify="center">
      <Grid item xs={4}>
        <Typography variant="body2" color="textSecondary" noWrap>
          {props.number + " " + props.name}
        </Typography>
      </Grid>
      <Grid item xs={8}>
        <StyledLinearProgress variant="determinate" value={props.number} />
      </Grid>
    </Grid>
  );
}

const useStyles = makeStyles((theme) => ({
  card: {
    height: "100%",
    width: "100%",
    display: "flex",
    flexDirection: "column",
  },
  cardMedia: {
    paddingTop: "56.25%", // 16:9
  },
  cardContent: {
    flexGrow: 1,
  },
  chipBox: {
    display: "flex",
    justifyContent: "center",
    flexWrap: "wrap",
    listStyle: "none",
    padding: theme.spacing(0.5),
    margin: 0,
  },
  chip: {
    margin: theme.spacing(0.5),
  },
  reviewBar: {
    width: "100%",
  },
  editButton: {
    marginTop: theme.spacing(0.5),
    color: "white",
    backgroundColor: theme.palette.success.main,
    "&:hover": {
      backgroundColor: theme.palette.success.light,
    },
  },
  deleteButton: {
    marginTop: theme.spacing(0.5),
    color: "white",
    backgroundColor: theme.palette.secondary.main,
    "&:hover": {
      backgroundColor: theme.palette.secondary.light,
    },
  },
}));

export default function ProductViewBody(props) {
  const classes = useStyles();
  const [called, setCalled] = useState(false);
  const baseDir = "/upload/product/profile_image/";
  const session = props.session;
  const productId = props.match.params.productId;
  const [product, setProduct] = useState(null);
  const dispatch = useDispatch();
  const loading = () => dispatch(sellerLoading());
  const done = () => dispatch(sellerDone());
  const GetProduct = async () => {
    loading();
    await axios
      .get("/seller/product/" + productId, {
        params: {
          getterz_session: session,
        },
      })
      .then(function (response) {
        if (response.data.resultCode === "OK") {
          setProduct(response.data.data);
        }
        done();
      })
      .catch(function (error) {
        done();
      });
  };
  if (!called) {
    setCalled(true);
    GetProduct();
  }
  const [openProductDelete, setOpenProductDelete] = useState(false);
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down("sm"));
  const handleClickOpenProductDelete = () => {
    setOpenProductDelete(true);
  };
  const handleCloseProductDelete = () => {
    setOpenProductDelete(false);
  };
  const history = useHistory();
  const redirecTo = useCallback(() => history.push("/seller/product"), [
    history,
  ]);
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
    redirecTo();
  };
  return product ? (
    <Grid
      container
      item
      spacing={1}
      xs={10}
      direction="column"
      alignItems="center"
    >
      <Grid container item spacing={1} xs={12} direction="row" justify="center">
        <Grid container spacing={1} item xs={6}>
          <Grid item xs={12}>
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
                <Typography>{product.quantity} left</Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} className={classes.chipBox}>
            {product.tags.map((t) => {
              return (
                <li>
                  <Chip label={t.name} className={classes.chip} />
                </li>
              );
            })}
          </Grid>
          <Grid container item xs={12}>
            <Grid container item xs={12} className={classes.reviewBar}>
              <LinearProgressWithLabel
                number={product.reviews.length}
                name={"Total reviews"}
                colorPrimary={"#303f9f"}
                barColorPrimary={"#3f51b5"}
              />
            </Grid>
            <Grid container item xs={12} className={classes.reviewBar}>
              <LinearProgressWithLabel
                number={
                  product.reviews.filter(
                    (r) =>
                      r.purchaseResult === "SUCCESS" &&
                      r.purchaseReason === "COMPLETE"
                  ).length
                }
                name={"Success complete products"}
                colorPrimary={"#388e3c"}
                barColorPrimary={"#4caf50"}
              />
            </Grid>
            <Grid container item xs={12} className={classes.reviewBar}>
              <LinearProgressWithLabel
                number={
                  product.reviews.filter(
                    (r) =>
                      r.purchaseResult === "SUCCESS" &&
                      r.purchaseReason === "INCOMPLETE"
                  ).length
                }
                name={"Success incomplete products"}
                colorPrimary={"#388e3c"}
                barColorPrimary={"#81c784"}
              />
            </Grid>
            <Grid container item xs={12} className={classes.reviewBar}>
              <LinearProgressWithLabel
                number={
                  product.reviews.filter(
                    (r) =>
                      r.purchaseResult === "FAIL" && r.purchaseReason === "MIA"
                  ).length
                }
                name={"Fail lost products"}
                colorPrimary={"#c51162"}
                barColorPrimary={"#f50057"}
              />
            </Grid>
            <Grid container item xs={12} className={classes.reviewBar}>
              <LinearProgressWithLabel
                number={
                  product.reviews.filter(
                    (r) =>
                      r.purchaseResult === "FAIL" &&
                      r.purchaseReason === "REFUND"
                  ).length
                }
                name={"Fail refund products"}
                colorPrimary={"#c51162"}
                barColorPrimary={"#ff4081"}
              />
            </Grid>
          </Grid>
        </Grid>
        <Grid container item xs={6}>
          <IframeResizer
            scrolling="yes"
            src={product.descriptionLink}
            style={{ width: "1px", minWidth: "100%" }}
          />
        </Grid>
      </Grid>
      <Grid container spacing={1} item xs={12}>
        <Grid item xs={6}>
          <Button fullWidth variant="contained" className={classes.editButton}>
            Edit
          </Button>
        </Grid>
        <Grid item xs={6}>
          <Button
            fullWidth
            variant="contained"
            className={classes.deleteButton}
            onClick={handleClickOpenProductDelete}
          >
            Delete
          </Button>
        </Grid>
      </Grid>
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
  ) : (
    <></>
  );
}
