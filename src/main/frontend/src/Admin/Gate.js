import React, { useCallback } from "react";
import { useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import HeadBar from "../Layout/HeadBar";
import BodyPaper from "../Layout/BodyPaper";
import FootBar from "../Layout/FootBar";
import GateBody from "./GateBody";

export default function Gate() {
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
    <div>
      <HeadBar Type="Admin" />
      <BodyPaper Type="Admin" InsideTag={<GateBody />} />
      <FootBar Type="Admin" />
    </div>
  );
}
