package framework.component.work;


@SuppressWarnings("serial")
public class DeleteLinkImage extends LinkBase {

	@Override
	protected ACTION_TYPE getActionType() {
		return ACTION_TYPE.DELETE;
	}
}
