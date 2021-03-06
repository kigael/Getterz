import React, { useCallback } from "react";
import { useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import { AdminHeadBar, AdminBodyPaper, AdminFootBar } from "../../Layout";
import BuyerVerifyBody from "./BuyerVerifyBody";

export default function BuyerVerify() {
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
      <AdminBodyPaper Body={<BuyerVerifyBody session={header.session} />} />
      <AdminFootBar />
    </>
  );
}
