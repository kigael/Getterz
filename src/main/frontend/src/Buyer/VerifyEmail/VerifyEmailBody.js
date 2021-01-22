import React, { useState } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { buyerLoading, buyerDone } from "../../info/BuyerInfo";
import qs from "qs";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";

const useStyle = makeStyles((theme) => ({
  error: {
    textAlign: "center",
    color: theme.palette.primary.main,
  },
}));

export default function VerifyEmailBody(props) {
  const classes = useStyle();
  const query = qs.parse(props.location.search, {
    ignoreQueryPrefix: true,
  });
  const [called, setCalled] = useState(false);
  const [message, setMessage] = useState("");
  const dispatch = useDispatch();
  const loading = () => dispatch(buyerLoading());
  const done = () => dispatch(buyerDone());
  const Verify = async (message, setMessage, query) => {
    loading();
    await axios
      .get("/buyer/verify_email", {
        params: {
          token: query.token,
        },
      })
      .then(function (response) {
        if (response.data.resultCode === "OK") {
          setMessage(
            "BUYER EMAIL VERIFICATION SUCCESS PLEASE WAIT FOR ADMIN CORFIRMATION"
          );
        } else if (response.data.resultCode === "ERROR") {
          setMessage(response.data.description);
        }
        done();
      })
      .catch(function (error) {
        done();
      });
  };
  if (!called) {
    setCalled(true);
    Verify(message, setMessage, query);
  }
  return (
    <div>
      {message === "" ? (
        <Typography className={classes.error} variant="h3">
          PROCESSING...
        </Typography>
      ) : (
        <Typography className={classes.error} variant="h3">
          {message}
        </Typography>
      )}
    </div>
  );
}
