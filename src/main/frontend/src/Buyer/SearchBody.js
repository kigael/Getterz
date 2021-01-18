import React, { useState } from "react";
import {
  Grid,
  TextField,
  Typography,
  Chip,
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
import NumberFormat from "react-number-format";
import { makeStyles } from "@material-ui/core/styles";
import Pagination from "@material-ui/lab/Pagination";

const useStyles = makeStyles((theme) => ({
  input: {
    "& label.Mui-focused": {
      color: "white",
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: theme.palette.primary.dark,
    },
    "& .MuiOutlinedInput-root": {
      "& fieldset": {
        borderColor: "white",
      },
      "&:hover fieldset": {
        borderColor: "white",
      },
      "&.Mui-focused fieldset": {
        borderColor: theme.palette.primary.dark,
      },
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
      borderBottomColor: theme.palette.primary.dark,
    },
    "& .MuiOutlinedInput-root": {
      "& fieldset": {
        borderColor: "white",
      },
      "&:hover fieldset": {
        borderColor: "white",
      },
      "&.Mui-focused fieldset": {
        borderColor: theme.palette.primary.dark,
      },
    },
  },
  heroContent: {
    backgroundColor: theme.palette.primary.dark,
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

function NumberFormatCustom(props) {
  const { inputRef, onChange, ...other } = props;
  return (
    <NumberFormat
      {...other}
      getInputRef={inputRef}
      onValueChange={(values) => {
        onChange({
          target: {
            name: props.name,
            value: values.value,
          },
        });
      }}
      thousandSeparator
      isNumericString
      prefix="$"
    />
  );
}

function DummyItem() {
  const classes = useStyles();
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
          <Typography style={{ color: "white", backgroundColor: "white" }}>
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

export default function SearchBody() {
  const classes = useStyles();
  const [products, setProducts] = useState([
    {
      id: null,
      name: "",
      cost: null,
      tags: [],
      quantity: null,
      profileImageName: "",
    },
    {
      id: null,
      name: "",
      cost: null,
      tags: [],
      quantity: null,
      profileImageName: "",
    },
    {
      id: null,
      name: "",
      cost: null,
      tags: [],
      quantity: null,
      profileImageName: "",
    },
    {
      id: null,
      name: "",
      cost: null,
      tags: [],
      quantity: null,
      profileImageName: "",
    },
    {
      id: null,
      name: "",
      cost: null,
      tags: [],
      quantity: null,
      profileImageName: "",
    },
    {
      id: null,
      name: "",
      cost: null,
      tags: [],
      quantity: null,
      profileImageName: "",
    },
    {
      id: null,
      name: "",
      cost: null,
      tags: [],
      quantity: null,
      profileImageName: "",
    },
    {
      id: null,
      name: "",
      cost: null,
      tags: [],
      quantity: null,
      profileImageName: "",
    },
  ]);
  const [tag, setTag] = useState("");
  const onTagChange = (e) => {
    setTag(e.target.value);
  };
  const [tagList, setTagList] = useState([]);
  const handleTagInsert = () => {
    if (!tagList.find((t) => t.name === tag)) {
      setTagList((tagList) => [...tagList, { name: tag }]);
    }
    setTag("");
  };
  const handleTagDelete = (tagToDelete) => () => {
    setTagList((tags) => tags.filter((tag) => tag.name !== tagToDelete.name));
  };
  const [sort, setSort] = useState(null);
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
        <Grid item xs={10}>
          <TextField
            className={classes.input}
            InputLabelProps={{ shrink: true, style: { color: "white" } }}
            id="productName"
            label="Product name"
            style={{ margin: 8 }}
            fullWidth
            size="small"
            margin="normal"
            variant="outlined"
          />
        </Grid>
        {tagList.length > 0 ? (
          <Grid container item xs={12} className={classes.tagBox}>
            {tagList.map((t) => {
              return (
                <li>
                  <Chip
                    label={t.name}
                    onDelete={handleTagDelete(t)}
                    className={classes.chip}
                  />
                </li>
              );
            })}
          </Grid>
        ) : (
          <></>
        )}
        <Grid container item xs={3} direction="column" alignItems="center">
          <TextField
            className={classes.input}
            InputLabelProps={{ style: { color: "white" } }}
            label="Minimum cost"
            name="minimumCost"
            id="minimumCost"
            size="small"
            InputProps={{ inputComponent: NumberFormatCustom }}
          />
        </Grid>
        <Grid container item xs={3} direction="column" alignItems="center">
          <TextField
            className={classes.input}
            InputLabelProps={{ style: { color: "white" } }}
            label="Maximum cost"
            name="maximumCost"
            id="maximumCost"
            size="small"
            InputProps={{ inputComponent: NumberFormatCustom }}
          />
        </Grid>
        <Grid container item xs={3} direction="column" alignItems="center">
          <TextField
            className={classes.input}
            InputLabelProps={{ style: { color: "white" } }}
            value={tag}
            onChange={onTagChange}
            name="tagInsert"
            variant="outlined"
            size="small"
            id="tagInsert"
            label="Tags"
            onKeyUp={(e) => {
              if (e.key === "Enter") {
                handleTagInsert();
              }
            }}
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
              <MenuItem value={null}>Don't sort</MenuItem>
              <MenuItem value={"ORDER_BY_DISTANCE_ASC"}>Nearest</MenuItem>
              <MenuItem value={"ORDER_BY_DISTANCE_DESC"}>Farthest</MenuItem>
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
          color="primary"
          className={classes.submit}
        >
          SEARCH
        </Button>
      </Grid>
      <Pagination
        className={classes.pageNavi}
        defaultPage={1}
        page={1}
        count={10}
        shape="rounded"
        color="primary"
      />
      <Grid container className={classes.cardGrid} spacing={2} xs={10}>
        {products.map((product) => {
          if (product.id) {
            return (
              <Grid item key={product} xs={3}>
                <Card className={classes.card}>
                  <CardMedia
                    className={classes.cardMedia}
                    image={product.profileImageName}
                    title={product.name}
                  />
                  <CardContent className={classes.cardContent}>
                    <Typography gutterBottom variant="h5" component="h2">
                      {product.name}
                    </Typography>
                    <Typography>${product.cost}</Typography>
                  </CardContent>
                  <CardActions>
                    <Button size="small" color="primary">
                      View
                    </Button>
                    <Button size="small" color="primary">
                      Purchase
                    </Button>
                  </CardActions>
                </Card>
              </Grid>
            );
          } else {
            return DummyItem();
          }
        })}
      </Grid>
    </>
  );
}
