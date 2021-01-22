import React, { useState, useCallback } from "react";
import { useHistory } from "react-router-dom";
import axios from "axios";
import { useDispatch } from "react-redux";
import { adminLoading, adminDone } from "../../info/AdminInfo";
import {
  Avatar,
  Button,
  TextField,
  Grid,
  InputLabel,
  FormControl,
  Select,
  MenuItem,
  Typography,
} from "@material-ui/core";
import MuiPhoneNumber from "material-ui-phone-number";
import { makeStyles } from "@material-ui/core/styles";

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.primary.main,
  },
  form: {
    width: "50%", // Fix IE 11 issue.
    marginTop: theme.spacing(3),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120,
  },
  container: {
    display: "flex",
    flexWrap: "wrap",
  },
  textField: {
    marginLeft: theme.spacing(1),
    marginRight: theme.spacing(1),
    width: 200,
  },
  icon: {
    color: theme.palette.text.secondary,
    marginRight: theme.spacing(2),
  },
  margin: {
    margin: theme.spacing(1),
  },
  input: {
    display: "none",
  },
  error: {
    textAlign: "center",
    color: theme.palette.primary.main,
  },
}));

export default function SellerVerifyCertifyBody(props) {
  const classes = useStyles();
  const verifyBasePath = "/upload/seller/verify_image/";
  const profileBasePath = "/upload/seller/profile_image/";
  const [called, setCalled] = useState(false);
  const session = props.session;
  const sellerId = props.match.params.sellerId;
  const [seller, setSeller] = useState(null);
  const [message, setMessage] = useState("");
  const dispatch = useDispatch();
  const loading = () => dispatch(adminLoading());
  const done = () => dispatch(adminDone());
  const [isImageOpen, setIsImageOpen] = useState(false);
  const handleImageOpen = () => {
    setIsImageOpen(!isImageOpen);
  };
  const handleMessageChange = (e) => {
    setMessage(e.target.value);
  };
  const history = useHistory();
  const redirecTo = useCallback(() => history.push("/admin/seller_verify"), [
    history,
  ]);
  const GetSeller = async () => {
    loading();
    await axios
      .get("/admin/seller_verify/" + sellerId, {
        params: {
          getterz_session: session,
        },
      })
      .then(function (response) {
        if (response.data.resultCode === "OK") {
          setSeller(response.data.data);
        } else if (response.data.resultCode === "ERROR") {
          console.log(response);
        }
        done();
      })
      .catch(function (error) {
        done();
      });
  };
  const Approve = async () => {
    loading();
    if (message !== "") {
      await axios
        .post("/admin/seller_verify", {
          transactionTime: "",
          transactionType: "ADMIN SELLER VERIFY",
          data: {
            certify: true,
            sellerId: seller.id,
            message: message,
          },
          session: session,
        })
        .then(function (response) {
          if (response.data.resultCode === "OK") {
            done();
            redirecTo();
          } else if (response.data.resultCode === "ERROR") {
            done();
            console.log(response);
          }
        })
        .catch(function (error) {
          done();
        });
    }
  };
  const Refuse = async () => {
    loading();
    if (message !== "") {
      await axios
        .post("/admin/seller_verify", {
          transactionTime: "",
          transactionType: "ADMIN SELLER VERIFY",
          data: {
            certify: false,
            sellerId: seller.id,
            message: message,
          },
          session: session,
        })
        .then(function (response) {
          if (response.data.resultCode === "OK") {
            done();
            redirecTo();
          } else if (response.data.resultCode === "ERROR") {
            done();
            console.log(response);
          }
        })
        .catch(function (error) {
          done();
        });
    }
  };
  if (!called) {
    setCalled(true);
    GetSeller();
  }
  return seller ? (
    <div className={classes.paper}>
      <Avatar
        className={classes.avatar}
        alt={seller.name}
        src={profileBasePath + seller.profileImageName}
      />
      <Typography component="h1" variant="h5">
        Seller Info
      </Typography>
      <form className={classes.form} noValidate>
        {isImageOpen ? (
          <>
            <img
              src={verifyBasePath + seller.verifyImageName}
              onClick={handleImageOpen}
              alt="job verification"
              style={{
                position: "absolute",
                zIndex: "1000",
                width: "50%",
                height: "50%",
              }}
            />
          </>
        ) : (
          <></>
        )}
        <Grid container spacing={2}>
          <Grid item xs={6} sm={6}>
            <TextField
              variant="outlined"
              fullWidth
              value={seller.name}
              label="Full Name"
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              variant="outlined"
              required
              fullWidth
              id="emailAddress"
              value={seller.emailAddress}
              label="Email Address"
              name="emailAddress"
            />
          </Grid>
          <Grid item xs={3}>
            <FormControl className={classes.formControl}>
              <InputLabel id="gender-select-label">Gender</InputLabel>
              <Select
                labelId="gender-select-label"
                label="Gender"
                value={seller.gender}
              >
                <MenuItem value={"MALE_STRAIGHT"}>MALE_STRAIGHT</MenuItem>
                <MenuItem value={"MALE_GAY"}>MALE_GAY</MenuItem>
                <MenuItem value={"MALE_BISEXUAL"}>MALE_BISEXUAL</MenuItem>
                <MenuItem value={"FEMALE_STRAIGHT"}>FEMALE_STRAIGHT</MenuItem>
                <MenuItem value={"FEMALE_GAY"}>FEMALE_GAY</MenuItem>
                <MenuItem value={"FEMALE_BISEXUAL"}>FEMALE_BISEXUAL</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={3}>
            <form className={classes.container} noValidate>
              <TextField
                value={seller.dateOfBirth}
                label="Date of birth"
                type="date"
                className={classes.textField}
                InputLabelProps={{
                  shrink: true,
                }}
              />
            </form>
          </Grid>
          <Grid item xs={6}>
            <MuiPhoneNumber
              name="cellNumber"
              value={seller.cellNumber}
              label="Cell Number"
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              variant="outlined"
              required
              fullWidth
              label="Address"
              value={seller.address}
            />
          </Grid>
          <Grid item xs={6}>
            <Button
              fullWidth
              variant="contained"
              color="primary"
              className={classes.submit}
              onClick={handleImageOpen}
            >
              Show job verifying image
            </Button>
          </Grid>
          <Grid item xs={12}>
            <TextField
              id="message"
              name="message"
              value={message}
              label="Message"
              fullWidth
              onChange={handleMessageChange}
              multiline
              rows={3}
              defaultValue="Message to seller"
              variant="outlined"
            />
          </Grid>
          <Grid item xs={6}>
            <Button
              fullWidth
              variant="contained"
              color="primary"
              className={classes.submit}
              onClick={Approve}
            >
              Approve
            </Button>
          </Grid>
          <Grid item xs={6}>
            <Button
              fullWidth
              variant="contained"
              color="secondary"
              className={classes.submit}
              onClick={Refuse}
            >
              Refuse
            </Button>
          </Grid>
        </Grid>
      </form>
    </div>
  ) : (
    <div></div>
  );
}
