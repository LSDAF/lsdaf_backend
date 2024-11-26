import {AdminGameSaveService} from "Frontend/generated/endpoints";
import gameSaveModel from "Frontend/generated/com/lsadf/core/models/GameSaveModel";
import React from "react";
import {AutoGrid} from "@vaadin/hilla-react-crud";
import {GridColumn} from "@vaadin/react-components";

function gameSaveDetailsButtonRenderer({item}: { item: gameSaveModel }) {
    return (
        <a href={`/gameSaves/${item.id}`} className="text-primary">Details</a>
    );
}


export default function GameSavesView() {
    return (
            <AutoGrid
                service={AdminGameSaveService}
                model={gameSaveModel}
                visibleColumns={['id', 'user_email', 'created_at', 'updated_at', 'nickname']}
                customColumns={[
                    <GridColumn key="details" header="Details" renderer={gameSaveDetailsButtonRenderer}/>
                ]}
            />
    )

}