import React, { useState } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { sellerLoading, sellerDone } from "../info/SellerInfo";
import {
  Grid,
  TextField,
  Typography,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Card,
  CardMedia,
  CardContent,
  CardActions,
  Button,
} from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import Pagination from "@material-ui/lab/Pagination";

const useStyles = makeStyles((theme) => ({
  input: {
    "& label.Mui-focused": {
      color: "white",
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: theme.palette.success.dark,
    },
    "& .MuiOutlinedInput-root": {
      "& fieldset": {
        borderColor: "white",
      },
      "&:hover fieldset": {
        borderColor: "white",
      },
      "&.Mui-focused fieldset": {
        borderColor: theme.palette.success.dark,
      },
    },
  },
  submit: {
    marginTop: theme.spacing(0.5),
    color: "white",
    backgroundColor: theme.palette.success.main,
    "&:hover": {
      backgroundColor: theme.palette.success.light,
    },
  },
  tagBox: {
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
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120,
    "& label.Mui-focused": {
      color: "white",
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: theme.palette.success.dark,
    },
    "& .MuiOutlinedInput-root": {
      "& fieldset": {
        borderColor: "white",
      },
      "&:hover fieldset": {
        borderColor: "white",
      },
      "&.Mui-focused fieldset": {
        borderColor: theme.palette.success.dark,
      },
    },
  },
  heroContent: {
    backgroundColor: theme.palette.success.dark,
    height: "20%",
    marginTop: theme.spacing(3),
    padding: theme.spacing(3),
    borderRadius: 30,
  },
  heroButtons: {
    marginTop: theme.spacing(4),
  },
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

export default function ProductBody(props) {
  const classes = useStyles();
  const baseDir = "/upload/product/profile_image/";
  const [called, setCalled] = useState(false);
  const [pagenate, setPagenate] = useState({
    page: 1,
    totalCount: 1,
  });
  const onPageChange = (event, page) => {
    setPagenate({
      ...pagenate,
      page: page,
    });
    SearchProducts();
  };
  const { page, totalCount } = pagenate;
  const [productName, setProductName] = useState("");
  const onProductNameChange = (e) => {
    setProductName(e.target.value);
  };
  const [products, setProducts] = useState([]);
  const [sort, setSort] = useState("DO_NOT_ORDER");
  const [openSort, setOpenSort] = useState(false);
  const onSortChange = (e) => {
    setSort(e.target.value);
  };
  const onSortClose = () => {
    setOpenSort(false);
  };
  const onSortOpen = () => {
    setOpenSort(true);
  };
  const dispatch = useDispatch();
  const loading = () => dispatch(sellerLoading());
  const done = () => dispatch(sellerDone());
  const SearchProducts = async () => {
    loading();
    await axios
      .get("/seller/product", {
        params: {
          productName: productName,
          orderByType: sort,
          getterz_session: props.session,
          page: pagenate.page - 1,
          size: 8,
        },
      })
      .then(function (response) {
        if (response.data.resultCode === "OK") {
          setProducts(response.data.data.products);
          for (let i = response.data.data.products.length; i < 8; i++) {
            setProducts((products) => [...products, null]);
          }
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
  if (!called) {
    setCalled(true);
    SearchProducts();
  }
  return (
    <>
      <Grid
        container
        xs={10}
        className={classes.heroContent}
        direction="row"
        justify="center"
        alignItems="flex-start"
      >
        <Grid item xs={9}>
          <TextField
            className={classes.input}
            InputLabelProps={{ shrink: true, style: { color: "white" } }}
            id="productName"
            name="productName"
            label="Product name"
            style={{ margin: 8 }}
            fullWidth
            size="small"
            margin="normal"
            variant="outlined"
            onChange={onProductNameChange}
            value={productName}
          />
        </Grid>
        <Grid container item xs={3} direction="column" alignItems="center">
          <FormControl className={classes.formControl}>
            <InputLabel id="sort-select-label" style={{ color: "white" }}>
              Sort
            </InputLabel>
            <Select
              labelId="sort-select-label"
              label="Sort"
              id="sortSelect"
              fullWidth
              size="small"
              open={openSort}
              onClose={onSortClose}
              onOpen={onSortOpen}
              value={sort}
              onChange={onSortChange}
            >
              <MenuItem value={"DO_NOT_ORDER"}>Don't sort</MenuItem>
              <MenuItem value={"ORDER_BY_REGISTER_DATE_ASC"}>Outdated</MenuItem>
              <MenuItem value={"ORDER_BY_REGISTER_DATE_DESC"}>Recent</MenuItem>
              <MenuItem value={"ORDER_BY_COST_ASC"}>Low-priced</MenuItem>
              <MenuItem value={"ORDER_BY_COST_DESC"}>High-priced</MenuItem>
              <MenuItem value={"ORDER_BY_REVIEW_ASC"}>Most-reviewed</MenuItem>
              <MenuItem value={"ORDER_BY_REVIEW_DESC"}>Least-reviewed</MenuItem>
            </Select>
          </FormControl>
        </Grid>
        <Button
          fullWidth
          variant="contained"
          className={classes.submit}
          onClick={SearchProducts}
        >
          SEARCH
        </Button>
        <Button
          fullWidth
          variant="contained"
          className={classes.submit}
          href="/#/seller/product/register"
        >
          REGISTER
        </Button>
      </Grid>
      <Pagination
        className={classes.pageNavi}
        defaultPage={1}
        page={page}
        count={totalCount}
        onChange={onPageChange}
        shape="rounded"
      />
      <Grid container className={classes.cardGrid} spacing={2} xs={10}>
        {products.map((product) => {
          if (product) {
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
                    <Button size="small">Edit</Button>
                    <Button size="small">Delete</Button>
                  </CardActions>
                </Card>
              </Grid>
            );
          } else {
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
                    <Typography
                      style={{ color: "white", backgroundColor: "white" }}
                    >
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
        })}
      </Grid>
    </>
  );
}
