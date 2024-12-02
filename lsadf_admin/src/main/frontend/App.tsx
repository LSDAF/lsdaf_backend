import GameSavesView from "Frontend/views/gameSaves/@index";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import UsersView from "Frontend/views/users/@index";
import Layout from "Frontend/views/@layout";


const App: React.FC = () => {
    return (
        <Router>
            <Layout>
                <Routes>
                    <Route key='/users' path="/users" element={<UsersView />} />
                    <Route key='/gameSaves' path="/gameSaves" element={<GameSavesView />} />
                </Routes>
            </Layout>
        </Router>
    );
}

export default App;