import {AdminUserService} from "Frontend/generated/endpoints";
import UserModel from "Frontend/generated/com/lsadf/core/models/UserModel";
import {AutoGrid} from "@vaadin/hilla-react-crud";
import React from "react";
import {Button} from "@vaadin/react-components";
import {getUserInfo} from "Frontend/generated/AdminUserInfoServiceImpl";
import {toast} from "react-hot-toast";

function notify() {
    getUserInfo()
        .then(userInfo => {
            if (userInfo === undefined) {
                toast.error('Failed to get user info');
                return;
            }
            console.log(userInfo);
            toast.success(userInfo?.name ?? 'Unknown');
        })
        .catch(err => {
            console.error(err);
            toast.error('Failed to get user info');
        })
}

export default function UsersView() {
    return (
        <span>
            <Button onClick={notify}>Make me a toast</Button>
            <AutoGrid service={AdminUserService} model={UserModel}/>
        </span>
    );
}