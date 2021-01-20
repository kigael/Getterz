import React, { useState, useCallback } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { useHistory } from "react-router-dom";
import Pagination from "@material-ui/lab/Pagination";
import { adminLoading, adminDone } from "../info/AdminInfo";
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

export default function BuyerVerifyBody(props) {
  const classes = useStyles();
  const baseDir = "/upload/buyer/profile_image/";
  const [called, setCalled] = useState(false);
  const [buyers, setBuyers] = useState([]);
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
      .get("/admin/buyer_verify", {
        params: {
          getterz_session: props.session,
          page: pagenate.page - 1,
          size: 8,
        },
      })
      .then(function (response) {
        if (response.data.resultCode === "OK") {
          setBuyers(response.data.data.buyers);
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
    (buyerId) => history.push("/admin/buyer_verify/" + buyerId),
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
        {buyers.map((buyer) => (
          <Grid item key={buyer} xs={3}>
            <Card className={classes.card}>
              <CardMedia
                className={classes.cardMedia}
                image={baseDir + buyer.profileImageName}
                title={buyer.name}
              />
              <CardContent className={classes.cardContent}>
                <Typography gutterBottom variant="h5" component="h2">
                  {buyer.name}
                </Typography>
                <Typography>{buyer.dateOfJoin}</Typography>
                <Typography>{buyer.emailAddress}</Typography>
                <Typography>{buyer.cellNumber}</Typography>
              </CardContent>
              <CardActions>
                <Button
                  size="small"
                  color="primary"
                  onClick={() => redirecTo(buyer.id)}
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
