import React, { useState, useCallback } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { useHistory } from "react-router-dom";
import Pagination from "@material-ui/lab/Pagination";
import { adminLoading, adminDone } from "../info/AdminInfo";
import {
  Typography,
  Grid,
  Avatar,
  Button,
  Card,
  CardHeader,
  CardContent,
  CardActions,
} from "@material-ui/core";

export default function BuyerVerifyBody(props) {
  const [called, setCalled] = useState(false);
  const [buyers, setBuyers] = useState([]);
  const [pagenate, setPagenate] = useState({
    page: 1,
    totalCount: 50,
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
          size: 10,
        },
      })
      .then(function (response) {
        console.log(response);
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
    <Grid container direction="column" alignItems="center">
      <Grid container direction="row" item xs={12}>
        <Grid item xs={1} />
        <Grid
          container
          item
          spacing={2}
          xs={10}
          direction="row"
          justify="flex-start"
          alignItems="flex-start"
          height="75%"
        >
          <Grid item xs={10} />
          {buyers.map((buyer) => (
            <Grid item xs={2}>
              <Card>
                <CardHeader
                  avatar={<Avatar src={buyer.emailAddress} />}
                  title={buyer.name}
                  subheader={buyer.dateOfJoin}
                />
                <CardContent>
                  <Typography variant="body2" component="p">
                    {buyer.emailAddress}
                  </Typography>
                  <Typography variant="body2" component="p">
                    {buyer.cellNumber}
                  </Typography>
                </CardContent>
                <CardActions>
                  <Button size="small" onClick={() => redirecTo(buyer.id)}>
                    CERTIFY
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          ))}
          <Grid item xs={10} />
        </Grid>
        <Grid item xs={1}></Grid>
      </Grid>
      <Grid item xs={12}>
        <Pagination
          defaultPage={1}
          page={page}
          count={totalCount}
          onChange={onPageChange}
        />
      </Grid>
    </Grid>
  );
}
