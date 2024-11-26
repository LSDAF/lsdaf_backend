import {AdminUserService} from "Frontend/generated/endpoints";
import UserModel from "Frontend/generated/com/lsadf/core/models/UserModel";
import Layout from "Frontend/views/layout";
import {AutoGrid} from "@vaadin/hilla-react-crud";

export default function UsersView() {
    return (
        <Layout>
            <AutoGrid service={AdminUserService} model={UserModel} />
        </Layout>
    );
}