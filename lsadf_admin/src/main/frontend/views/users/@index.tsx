import {AdminUserService} from "Frontend/generated/endpoints";
import UserModel from "Frontend/generated/com/lsadf/core/models/UserModel";
import {AutoGrid} from "@vaadin/hilla-react-crud";
import React from "react";

export default function UsersView() {
    return (
            <AutoGrid service={AdminUserService} model={UserModel} />
    );
}