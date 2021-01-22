import React, { useCallback } from "react";
import { useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import { DefaultHeadBar, DefaultBodyPaper, DefaultFootBar } from "../../Layout";
import LoginBody from "./LoginBody";

export default function Login() {
  const { header, loading } = useSelector((state) => ({
    header: state.AdminInfo.header,
    loading: state.AdminInfo.loading,
  }));
  const history = useHistory();
  const redirecTo = useCallback(() => history.push("/admin"), [history]);
  if (!loading && header.session) {
    redirecTo();
  }
  return (
    <div>
      <DefaultHeadBar />
      <DefaultBodyPaper Body={<LoginBody />} />
      <DefaultFootBar />
    </div>
  );
}
