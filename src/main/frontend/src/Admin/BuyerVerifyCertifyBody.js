import React, { useState } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { adminLoading, adminDone } from "../info/AdminInfo";
import {
  Avatar,
  Button,
  TextField,
  Grid,
  InputLabel,
  FormControl,
  Select,
  MenuItem,
  Chip,
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
  jobBox: {
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

export default function BuyerVerifyCertifyBody(props) {
  const classes = useStyles();
  const verifyBasePath = "/upload/buyer/verify_image/";
  const profileBasePath = "/upload/buyer/profile_image/";
  const [called, setCalled] = useState(false);
  const session = props.session;
  const buyerId = props.match.params.buyerId;
  const [buyer, setBuyer] = useState(null);
  const dispatch = useDispatch();
  const loading = () => dispatch(adminLoading());
  const done = () => dispatch(adminDone());
  const [isImageOpen, setIsImageOpen] = useState(false);
  const handleImageOpen = () => {
    setIsImageOpen(!isImageOpen);
  };
  const GetBuyer = async () => {
    loading();
    await axios
      .get("/admin/buyer_verify/" + buyerId, {
        params: {
          getterz_session: session,
        },
      })
      .then(function (response) {
        if (response.data.resultCode === "OK") {
          setBuyer(response.data.data);
        } else if (response.data.resultCode === "ERROR") {
          console.log(response);
        }
        done();
      })
      .catch(function (error) {
        done();
      });
  };
  if (!called) {
    setCalled(true);
    GetBuyer();
  }
  return buyer ? (
    <div className={classes.paper}>
      <Avatar
        className={classes.avatar}
        alt={buyer.name}
        src={profileBasePath + buyer.profileImageName}
      />
      <Typography component="h1" variant="h5">
        Buyer Info
      </Typography>
      <form className={classes.form} noValidate>
        {isImageOpen ? (
          <>
            <img
              src={verifyBasePath + buyer.verifyImageName}
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
              value={buyer.name}
              label="Full Name"
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              variant="outlined"
              required
              fullWidth
              id="emailAddress"
              value={buyer.emailAddress}
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
                value={buyer.gender}
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
                value={buyer.dateOfBirth}
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
              value={buyer.cellNumber}
              label="Cell Number"
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              variant="outlined"
              required
              fullWidth
              label="Address"
              value={buyer.address}
            />
          </Grid>
          <Grid item xs={6} className={classes.jobBox}>
            {buyer.jobs.length > 0 ? (
              buyer.jobs.map((j) => {
                return (
                  <li>
                    <Chip label={j.name} className={classes.chip} />
                  </li>
                );
              })
            ) : (
              <Typography variant="body2" color="textSecondary">
                Job List
              </Typography>
            )}
          </Grid>
          <Grid item xs={6}>
            <TextField
              value={buyer.cryptoWallet}
              variant="outlined"
              fullWidth
              label="Bitcoin Wallet Address"
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
          <Grid item xs={6}>
            <Button
              fullWidth
              variant="contained"
              color="primary"
              className={classes.submit}
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
