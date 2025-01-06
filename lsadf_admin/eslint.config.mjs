import path from "node:path";
import {fileURLToPath} from "node:url";

import {fixupPluginRules} from "@eslint/compat";
import {FlatCompat} from "@eslint/eslintrc";
import js from "@eslint/js";
import typescriptEslint from "@typescript-eslint/eslint-plugin";
import tsParser from "@typescript-eslint/parser";
import _import from "eslint-plugin-import";
import jsxA11Y from "eslint-plugin-jsx-a11y";
import react from "eslint-plugin-react";
import globals from "globals";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const compat = new FlatCompat({
    baseDirectory: __dirname,
    recommendedConfig: js.configs.recommended,
    allConfig: js.configs.all
});

export default [{
    ignores: ["**/node_modules/", "**/target/", 'esling.config.mjs', 'src/main/frontend/generated/'],
}, ...compat.extends("eslint:recommended"), {
    plugins: {
        react,
        "@typescript-eslint": typescriptEslint,
        "jsx-a11y": jsxA11Y,
        import: fixupPluginRules(_import),
    },

    languageOptions: {
        globals: {
            ...globals.node,
        },

        parser: tsParser,
        ecmaVersion: "latest",
        sourceType: "module",

        parserOptions: {
            ecmaFeatures: {
                jsx: true,
            },
        },
    },

    settings: {
        react: {
            version: "detect",
        },

        "import/resolver": {
            node: {
                extensions: [".js", ".jsx", ".ts", ".tsx"],
            },
        },
    },

    rules: {
        "react/jsx-uses-react": "off",
        "react/react-in-jsx-scope": "off",
        "react/prop-types": "off",

        "@typescript-eslint/no-unused-vars": ["warn", {
            argsIgnorePattern: "^_",
            varsIgnorePattern: "^_",
        }],

        "@typescript-eslint/explicit-function-return-type": "off",
        "@typescript-eslint/no-explicit-any": "warn",
        "@typescript-eslint/no-empty-function": "warn",
        "jsx-a11y/anchor-is-valid": "warn",
        "jsx-a11y/no-autofocus": "warn",

        "import/order": ["error", {
            groups: ["builtin", "external", "internal", "parent", "sibling", "index"],
            "newlines-between": "always",

            alphabetize: {
                order: "asc",
                caseInsensitive: true,
            },
        }],

        "no-console": ["warn", {
            allow: ["warn", "error"],
        }],

        "no-debugger": "warn",

        "react/jsx-sort-props": ["error", {
            callbacksLast: true,
            shorthandFirst: true,
            reservedFirst: true,
        }],

        "react/no-unescaped-entities": "warn",
        "react/self-closing-comp": "error",

        "react/jsx-curly-brace-presence": ["error", {
            props: "never",
            children: "never",
        }],
    },
}];