import React, { useState, useCallback } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { useHistory } from "react-router-dom";
import Pagination from "@material-ui/lab/Pagination";
import { adminLoading, adminDone } from "../../info/AdminInfo";
import {
  Typography,
  Grid,
  Button,
  Card,
  CardActions,
  CardContent,
  CardMedia,
} from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";

const useStyles = makeStyles((theme) => ({
  pageNavi: {
    marginTop: theme.spacing(1),
    marginBottom: theme.spacing(0),
  },
  cardGrid: {
    height: "30%",
    marginTop: theme.spacing(0),
    marginBottom: theme.spacing(8),
  },
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

export default function SellerVerifyBody(props) {
  const classes = useStyles();
  const baseDir = "/upload/seller/profile_image/";
  const [called, setCalled] = useState(false);
  const [sellers, setSellers] = useState([]);
  const [pagenate, setPagenate] = useState({
    page: 1,
    totalCount: 1,
  });
  const dispatch = useDispatch();
  const loading = () => dispatch(adminLoading());
  const done = () => dispatch(adminDone());
  const GetList = async () => {
    loading();
    await axios
      .get("/admin/seller_verify", {
        params: {
          getterz_session: props.session,
          page: pagenate.page - 1,
          size: 8,
        },
      })
      .then(function (response) {
        console.log(response);
        if (response.data.resultCode === "OK") {
          setSellers(response.data.data.sellers);
          setPagenate({
            ...pagenate,
            page: response.data.currentPage + 1,
            totalCount: response.data.totalPage,
          });
        } else if (response.data.resultCode === "ERROR") {
          console.log(response);
        }
        done();
      })
      .catch(function (error) {
        done();
      });
  };
  const onPageChange = (event, page) => {
    setPagenate({
      ...pagenate,
      page: page,
    });
    GetList();
  };
  const { page, totalCount } = pagenate;
  const history = useHistory();
  const redirecTo = useCallback(
    (sellerId) => history.push("/admin/seller_verify/" + sellerId),
    [history]
  );
  if (!called) {
    setCalled(true);
    GetList();
  }
  return (
    <>
      <Pagination
        className={classes.pageNavi}
        defaultPage={1}
        page={page}
        count={totalCount}
        onChange={onPageChange}
        shape="rounded"
      />
      <Grid container className={classes.cardGrid} spacing={2} xs={10}>
        {sellers.map((seller) => (
          <Grid item key={seller} xs={3}>
            <Card className={classes.card}>
              <CardMedia
                className={classes.cardMedia}
                image={baseDir + seller.profileImageName}
                title={seller.name}
              />
              <CardContent className={classes.cardContent}>
                <Typography gutterBottom variant="h5" component="h2">
                  {seller.name}
                </Typography>
                <Typography>{seller.dateOfJoin}</Typography>
                <Typography>{seller.emailAddress}</Typography>
                <Typography>{seller.cellNumber}</Typography>
              </CardContent>
              <CardActions>
                <Button
                  size="small"
                  color="primary"
                  onClick={() => redirecTo(seller.id)}
                >
                  Certify
                </Button>
              </CardActions>
            </Card>
          </Grid>
        ))}
      </Grid>
    </>
  );
}
