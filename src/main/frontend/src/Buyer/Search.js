import React, { useCallback } from "react";
import { useHistory } from "react-router-dom";
import { useSelector } from "react-redux";
import BuyerHeadBar from "../Layout/BuyerHeadBar";
import BuyerBodyPaper from "../Layout/BuyerBodyPaper";
import BuyerFootBar from "../Layout/BuyerFootBar";
import SearchBody from "./SearchBody";

function Search() {
  const { header, loading } = useSelector((state) => ({
    header: state.BuyerInfo.header,
    loading: state.BuyerInfo.loading,
  }));
  const history = useHistory();
  const redirecTo = useCallback(() => history.push("/buyer/login"), [history]);
  if (!loading && !header.session) {
    redirecTo();
  }
  return (
    <>
      <BuyerHeadBar />
      <BuyerBodyPaper Body={<SearchBody />} />
      <BuyerFootBar />
    </>
  );
}

export default Search;
