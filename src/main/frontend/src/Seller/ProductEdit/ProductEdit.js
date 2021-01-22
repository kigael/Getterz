import React, { useCallback } from "react";
import { useHistory } from "react-router-dom";
import { useSelector } from "react-redux";
import { SellerHeadBar, SellerBodyPaper, SellerFootBar } from "../../Layout";
import ProductEditBody from "./ProductEditBody";

export default function ProductEdit({ match }) {
  const { header, loading } = useSelector((state) => ({
    header: state.SellerInfo.header,
    loading: state.SellerInfo.loading,
  }));
  const history = useHistory();
  const redirecTo = useCallback(() => history.push("/seller/login"), [history]);
  if (!loading && !header.session) {
    redirecTo();
  }
  return (
    <>
      <SellerHeadBar />
      <SellerBodyPaper
        Body={<ProductEditBody session={header.session} match={match} />}
      />
      <SellerFootBar />
    </>
  );
}
