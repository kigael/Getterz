import React, { useCallback } from "react";
import { useHistory } from "react-router-dom";
import { useSelector } from "react-redux";
import { SellerHeadBar, SellerBodyPaper, SellerFootBar } from "../../Layout";
import GateBody from "./GateBody";

export default function Gate() {
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
      <SellerBodyPaper Body={<GateBody />} />
      <SellerFootBar />
    </>
  );
}
