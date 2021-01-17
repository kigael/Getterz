import React, { useCallback } from "react";
import { useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import HeadBar from "../Layout/HeadBar";
import BodyPaper from "../Layout/BodyPaper";
import FootBar from "../Layout/FootBar";
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
      <HeadBar Type="Admin" />
      <BodyPaper
        Type="Admin"
        InsideTag={<BuyerVerifyBody session={header.session} />}
      />
      <FootBar Type="Admin" />
    </>
  );
}
