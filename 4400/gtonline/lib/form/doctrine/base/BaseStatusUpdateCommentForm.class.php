<?php

/**
 * StatusUpdateComment form base class.
 *
 * @method StatusUpdateComment getObject() Returns the current form's model object
 *
 * @package    gtonline
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfDoctrineFormGeneratedTemplate.php 29553 2010-05-20 14:33:00Z Kris.Wallsmith $
 */
abstract class BaseStatusUpdateCommentForm extends BaseFormDoctrine
{
  public function setup()
  {
    $this->setWidgets(array(
      'id'             => new sfWidgetFormInputHidden(),
      'statusUpdateId' => new sfWidgetFormDoctrineChoice(array('model' => $this->getRelatedModelName('StatusUpdate'), 'add_empty' => false)),
      'created_at'     => new sfWidgetFormInputText(),
      'text'           => new sfWidgetFormTextarea(),
      'userId'         => new sfWidgetFormDoctrineChoice(array('model' => $this->getRelatedModelName('User'), 'add_empty' => false)),
    ));

    $this->setValidators(array(
      'id'             => new sfValidatorChoice(array('choices' => array($this->getObject()->get('id')), 'empty_value' => $this->getObject()->get('id'), 'required' => false)),
      'statusUpdateId' => new sfValidatorDoctrineChoice(array('model' => $this->getRelatedModelName('StatusUpdate'))),
      'created_at'     => new sfValidatorPass(),
      'text'           => new sfValidatorString(array('max_length' => 512)),
      'userId'         => new sfValidatorDoctrineChoice(array('model' => $this->getRelatedModelName('User'))),
    ));

    $this->widgetSchema->setNameFormat('status_update_comment[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    $this->setupInheritance();

    parent::setup();
  }

  public function getModelName()
  {
    return 'StatusUpdateComment';
  }

}
