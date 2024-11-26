import {useParams} from "react-router-dom";
import {useSignal} from "@vaadin/hilla-react-signals";
import GameSaveModel from "Frontend/generated/com/lsadf/core/models/GameSaveModel";
import {useEffect} from "react";
import {AdminGameSaveService} from "Frontend/generated/endpoints";
import GameSave from "Frontend/generated/com/lsadf/core/models/GameSave";

export default function GameSaveView() {
    const { gameSaveId } = useParams<{ gameSaveId: string }>();
    if (!gameSaveId) {
        throw new Error("No game save ID provided");
    }

    const gameSave = useSignal<GameSave | undefined>(undefined);

    useEffect(() => {
        AdminGameSaveService.get(gameSaveId).then(p => gameSave.value = p);
    }, [gameSaveId]);
}