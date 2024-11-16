import {AutoCrud} from "@vaadin/hilla-react-crud";
import {AdminGameSaveService} from "Frontend/generated/endpoints";
import gameSaveModel from "Frontend/generated/com/lsadf/core/models/GameSaveModel";
import Layout from "Frontend/views/layout";


export default function GameSavesView() {
    // @ts-ignore
    return (
        <Layout>
            <AutoCrud service={AdminGameSaveService} model={gameSaveModel} />
        </Layout>
    )

}