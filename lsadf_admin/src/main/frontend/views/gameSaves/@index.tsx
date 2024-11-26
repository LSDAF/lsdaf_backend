import {AdminGameSaveService} from "Frontend/generated/endpoints";
import gameSaveModel from "Frontend/generated/com/lsadf/core/models/GameSaveModel";
import Layout from "Frontend/views/layout";
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
        <Layout>
            {/*<AutoCrud service={AdminGameSaveService} model={gameSaveModel} />*/}
            <AutoGrid
                service={AdminGameSaveService}
                model={gameSaveModel}
                visibleColumns={['id', 'user_email', 'created_at', 'updated_at', 'nickname']}
                customColumns={[
                    <GridColumn key="details" header="Details" renderer={gameSaveDetailsButtonRenderer}/>
                ]}
            />
        </Layout>
    )

}