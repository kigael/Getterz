import React, { useCallback } from "react";
import { useHistory } from "react-router-dom";
import { useSelector } from "react-redux";
import { BuyerHeadBar, BuyerBodyPaper, BuyerFootBar } from "../../Layout";
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
      <BuyerBodyPaper Body={<SearchBody session={header.session} />} />
      <BuyerFootBar />
    </>
  );
}

export default Search;
