import React, { useCallback } from "react";
import { useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import { AdminHeadBar, AdminBodyPaper, AdminFootBar } from "../../Layout";
import SellerVerifyBody from "./SellerVerifyBody";

export default function SellerVerify() {
  const { header, loading } = useSelector((state) => ({
    header: state.AdminInfo.header,
    loading: state.AdminInfo.loading,
  }));
  const history = useHistory();
  const redirecTo = useCallback(() => history.push("/"), [history]);
  if (!loading && !header.session) {
    redirecTo();
  }
  return (
    <>
      <AdminHeadBar />
      <AdminBodyPaper Body={<SellerVerifyBody session={header.session} />} />
      <AdminFootBar />
    </>
  );
}
