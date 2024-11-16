import {AutoCrud} from "@vaadin/hilla-react-crud";
import {AdminUserService} from "Frontend/generated/endpoints";
import UserModel from "Frontend/generated/com/lsadf/core/models/UserModel";
import Layout from "Frontend/views/layout";

export default function UsersView() {
    // @ts-ignore
    return (
        <Layout>
            <AutoCrud service={AdminUserService} model={UserModel} />
        </Layout>
    );
}