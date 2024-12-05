import {AdminGameSaveService} from "Frontend/generated/endpoints";
import gameSaveModel from "Frontend/generated/com/lsadf/core/models/GameSaveModel";
import React from "react";
import {AutoGrid} from "@vaadin/hilla-react-crud";
import {Button, GridColumn} from "@vaadin/react-components";
import {toast} from "react-hot-toast";
import {getUserInfo} from "Frontend/generated/AdminUserInfoServiceImpl";
import {ViewConfig} from "@vaadin/hilla-file-router/types.js";

function gameSaveDetailsButtonRenderer({item}: { item: gameSaveModel }) {
    return (
        <a href={`/gameSaves/${item.id}`} className="text-primary">Details</a>
    );
}

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

export default function GameSavesView() {
    return (
        <span>
            <Button onClick={notify}>Make me a toast</Button>
            <AutoGrid
                service={AdminGameSaveService}
                model={gameSaveModel}
                visibleColumns={['id', 'user_email', 'created_at', 'updated_at', 'nickname']}
                customColumns={[
                    <GridColumn key="details" header="Details" renderer={gameSaveDetailsButtonRenderer}/>
                ]}
            />
        </span>
    )
}